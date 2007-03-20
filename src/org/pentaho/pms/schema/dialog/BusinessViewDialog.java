/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/


package org.pentaho.pms.schema.dialog;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.pms.locale.Locales;
import org.pentaho.pms.schema.BusinessView;
import org.pentaho.pms.schema.concept.ConceptInterface;
import org.pentaho.pms.schema.concept.dialog.ConceptDefaultsDialog;
import org.pentaho.pms.schema.security.SecurityReference;
import org.pentaho.pms.util.Const;

import be.ibridge.kettle.core.LogWriter;
import be.ibridge.kettle.core.Props;
import be.ibridge.kettle.core.WindowProperty;
import be.ibridge.kettle.core.dialog.ErrorDialog;
import be.ibridge.kettle.core.list.ObjectAlreadyExistsException;
import be.ibridge.kettle.trans.step.BaseStepDialog;


/***
 * Represents a business view
 * 
 * @since 30-aug-2006
 *
 */
public class BusinessViewDialog extends Dialog
{
    private LogWriter    log;

    private Label        wlName;
    private Text         wName;
    private FormData     fdlName, fdName;

    private Button wOK, wCancel;
    private Listener lsOK, lsCancel;

    private Shell         shell;
    
    private SelectionAdapter lsDef;
    private Props props;

    private BusinessView businessView;
    private String           viewId;
    
    private Map widgetInterfaces;

    private Locales locales;

    private SecurityReference securityReference;

    private Composite propertiesComposite;

    private ConceptInterface conceptInterface;

    private BusinessView originalView;

    public BusinessViewDialog(Shell parent, BusinessView businessView, Locales locales, SecurityReference securityReference)
    {
        super(parent, SWT.NONE);
        this.originalView = businessView;
        this.businessView = (BusinessView) businessView.clone();
        this.locales = locales;
        this.securityReference = securityReference;
        
        log=LogWriter.getInstance();
        props=Props.getInstance();
        
        conceptInterface = businessView.getConcept();

        widgetInterfaces = new Hashtable();
    }

    public String open()
    {
        Shell parent = getParent();
        Display display = parent.getDisplay();
        
        shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN);
        props.setLook(shell);
        
        log.logDebug(this.getClass().getName(), "Opening dialog");

        ModifyListener lsMod = new ModifyListener() 
        {
            public void modifyText(ModifyEvent e) 
            {
                businessView.setChanged();
            }
        };

        FormLayout formLayout = new FormLayout ();
        formLayout.marginWidth  = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;

        shell.setLayout(formLayout);
        shell.setText("Business view Properties");
        
        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        // Name line
        wlName=new Label(shell, SWT.RIGHT);
        wlName.setText("Name / ID");
        props.setLook(wlName);
        fdlName=new FormData();
        fdlName.left = new FormAttachment(0, 0);
        fdlName.right= new FormAttachment(middle, -margin);
        fdlName.top  = new FormAttachment(0, margin);
        wlName.setLayoutData(fdlName);
        wName=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        wName.setText("");
        props.setLook(wName);
        wName.addModifyListener(lsMod);
        fdName=new FormData();
        fdName.left = new FormAttachment(middle, 0);
        fdName.top  = new FormAttachment(0, margin);
        fdName.right= new FormAttachment(middle, 350);
        wName.setLayoutData(fdName);

        wOK=new Button(shell, SWT.PUSH);
        wOK.setText(" &OK ");
        wCancel=new Button(shell, SWT.PUSH);
        wCancel.setText(" &Cancel ");
        
        BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin, null);
        
        Composite composite = new Composite(shell, SWT.NONE);
        FormLayout compLayout = new FormLayout();
        composite.setLayout(compLayout);
        
        props.setLook(composite);
        
        propertiesComposite = new Composite(composite, SWT.NONE);
        propertiesComposite.setLayout(new FormLayout());
        props.setLook(propertiesComposite);
        
        ConceptDefaultsDialog.getControls(propertiesComposite, businessView, "Business view properties", conceptInterface, widgetInterfaces, locales, securityReference);
        
        FormData fdRight = new FormData();
        fdRight.top    = new FormAttachment(0, 0);
        fdRight.left   = new FormAttachment(props.getMiddlePct()/2, Const.MARGIN);
        fdRight.bottom = new FormAttachment(100, 0);
        fdRight.right  = new FormAttachment(100, 0);
        propertiesComposite.setLayoutData(fdRight);

        FormData fdComposite = new FormData();
        fdComposite.left   = new FormAttachment(0,0);
        fdComposite.right  = new FormAttachment(100,0);
        fdComposite.top    = new FormAttachment(wName, margin);
        fdComposite.bottom = new FormAttachment(wOK, -margin);
        composite.setLayoutData(fdComposite);

        // Add listeners
        lsCancel = new Listener() { public void handleEvent(Event e) { cancel();  } };
        lsOK     = new Listener() { public void handleEvent(Event e) { ok(); } };
        
        wCancel.addListener(SWT.Selection, lsCancel );
        wOK    .addListener(SWT.Selection, lsOK );

        lsDef=new SelectionAdapter() { public void widgetDefaultSelected(SelectionEvent e) { ok(); } };
        
        wName.addSelectionListener(lsDef);      

        // Detect X or ALT-F4 or something that kills this window...
        shell.addShellListener( new ShellAdapter() { public void shellClosed(ShellEvent e) { cancel(); } } );

        WindowProperty winprop = props.getScreen(shell.getText());
        if (winprop!=null) winprop.setShell(shell); else shell.pack();

        getData();
        
        shell.open();
        while (!shell.isDisposed())
        {
                if (!display.readAndDispatch()) display.sleep();
        }
        return viewId;
    }

    public void dispose()
    {
        props.setScreen(new WindowProperty(shell));
        shell.dispose();
    }
    
    /**
     * Copy information from the meta-data input to the dialog fields.
     */ 
    public void getData()
    {
        if (businessView.getId()!=null) wName.setText(businessView.getId());
 
        wName.selectAll();
    }
    
    private void cancel()
    {
        viewId=null;
        dispose();
    }

    protected void refreshConceptProperties()
    {
        ConceptDefaultsDialog.getControls(propertiesComposite, "Business view properties", conceptInterface, widgetInterfaces, locales, securityReference);
        propertiesComposite.layout(true, true);
    }

    private void ok()
    {
        try
        {
            originalView.setId(wName.getText());
        }
        catch (ObjectAlreadyExistsException e)
        {
            new ErrorDialog(shell, "Error", "A business view with ID '"+wName.getText()+"' already exists.", e);
            return;
        }

        // Clear the properties
        businessView.getConcept().clearChildProperties();

        // Get the widget values back...
        ConceptDefaultsDialog.setPropertyValues(shell, widgetInterfaces);

        // Copy these to the business table concept
        businessView.getConcept().getChildPropertyInterfaces().putAll(conceptInterface.getChildPropertyInterfaces());

        // The concept stuff: just overwrite it, there are no references from/to these...
        originalView.setConcept(businessView.getConcept());
        
        viewId = wName.getText();
        dispose();
    }
}
