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
package org.pentaho.pms.schema.concept.types;

public class ConceptPropertyType
{
    public static final int PROPERTY_TYPE_STRING           =  0;  
    public static final int PROPERTY_TYPE_DATE             =  1; 
    public static final int PROPERTY_TYPE_NUMBER           =  2;
    public static final int PROPERTY_TYPE_COLOR            =  3;
    public static final int PROPERTY_TYPE_FONT             =  4;
    public static final int PROPERTY_TYPE_FIELDTYPE        =  5;
    public static final int PROPERTY_TYPE_AGGREGATION      =  6;
    public static final int PROPERTY_TYPE_BOOLEAN          =  7;
    public static final int PROPERTY_TYPE_DATATYPE         =  8;
    public static final int PROPERTY_TYPE_LOCALIZED_STRING =  9;
    public static final int PROPERTY_TYPE_TABLETYPE        = 10;
    public static final int PROPERTY_TYPE_URL              = 11;
    public static final int PROPERTY_TYPE_SECURITY         = 12;
    public static final int PROPERTY_TYPE_ALIGNMENT        = 13;
    public static final int PROPERTY_TYPE_COLUMN_WIDTH     = 14;

    /**
     * These are the core property types to bootstrap the property system.
     */
    private static final ConceptPropertyType[] propertyTypes = 
        {
            new ConceptPropertyType( PROPERTY_TYPE_STRING,           "String",      "String"),
            new ConceptPropertyType( PROPERTY_TYPE_DATE,             "Date",        "Date"),
            new ConceptPropertyType( PROPERTY_TYPE_NUMBER,           "Number",      "Numeric value"),
            new ConceptPropertyType( PROPERTY_TYPE_COLOR,            "Color",       "Color"),
            new ConceptPropertyType( PROPERTY_TYPE_FONT,             "Font",        "Font"),
            new ConceptPropertyType( PROPERTY_TYPE_FIELDTYPE,        "FieldType",   "Type of field"),
            new ConceptPropertyType( PROPERTY_TYPE_AGGREGATION,      "Aggregation", "Type of aggregation"),
            new ConceptPropertyType( PROPERTY_TYPE_BOOLEAN,          "Boolean",     "Boolean"),
            new ConceptPropertyType( PROPERTY_TYPE_DATATYPE,         "DataType",    "Field data type"),
            new ConceptPropertyType( PROPERTY_TYPE_LOCALIZED_STRING, "LocString",   "Localized string"),
            new ConceptPropertyType( PROPERTY_TYPE_TABLETYPE,        "TableType",   "Type of table"),
            new ConceptPropertyType( PROPERTY_TYPE_URL,              "URL",         "URL"),
            new ConceptPropertyType( PROPERTY_TYPE_SECURITY,         "Security",    "Security"),
            new ConceptPropertyType( PROPERTY_TYPE_ALIGNMENT,        "Alignment",   "Text alignment"),
            new ConceptPropertyType( PROPERTY_TYPE_COLUMN_WIDTH,     "ColumnWidth", "Column Width"),
        };
    
    public static final ConceptPropertyType STRING           = propertyTypes[ PROPERTY_TYPE_STRING ];  
    public static final ConceptPropertyType DATE             = propertyTypes[ PROPERTY_TYPE_DATE ];  
    public static final ConceptPropertyType NUMBER           = propertyTypes[ PROPERTY_TYPE_NUMBER ];  
    public static final ConceptPropertyType COLOR            = propertyTypes[ PROPERTY_TYPE_COLOR ];  
    public static final ConceptPropertyType FONT             = propertyTypes[ PROPERTY_TYPE_FONT ];  
    public static final ConceptPropertyType FIELDTYPE        = propertyTypes[ PROPERTY_TYPE_FIELDTYPE ];  
    public static final ConceptPropertyType AGGREGATION      = propertyTypes[ PROPERTY_TYPE_AGGREGATION ];
    public static final ConceptPropertyType BOOLEAN          = propertyTypes[ PROPERTY_TYPE_BOOLEAN ];
    public static final ConceptPropertyType DATATYPE         = propertyTypes[ PROPERTY_TYPE_DATATYPE ];
    public static final ConceptPropertyType LOCALIZED_STRING = propertyTypes[ PROPERTY_TYPE_LOCALIZED_STRING ];
    public static final ConceptPropertyType TABLETYPE        = propertyTypes[ PROPERTY_TYPE_TABLETYPE ];
    public static final ConceptPropertyType URL              = propertyTypes[ PROPERTY_TYPE_URL ];
    public static final ConceptPropertyType SECURITY         = propertyTypes[ PROPERTY_TYPE_SECURITY ];
    public static final ConceptPropertyType ALIGNMENT        = propertyTypes[ PROPERTY_TYPE_ALIGNMENT ];
    public static final ConceptPropertyType COLUMN_WIDTH     = propertyTypes[ PROPERTY_TYPE_COLUMN_WIDTH];
    
    public static final String ISO_DATE_FORMAT = "yyyy/MM/dd'T'HH:mm:ss";
    
    /**
     * @return an array of the core concept property type descriptions
     */
    public static String[] getTypeDescriptions()
    {
        String types[] = new String[propertyTypes.length];
        for (int i=0;i<types.length;i++)
        {
            types[i] = propertyTypes[i].getDescription();
        }
        return types;
    }
    
    /**
     * @return an array of the core concept property type codes
     */
    public static String[] getTypeCodes()
    {
        String types[] = new String[propertyTypes.length];
        for (int i=0;i<types.length;i++)
        {
            types[i] = propertyTypes[i].getCode();
        }
        return types;
    }

    private int type;
    private String code;
    private String description;
    
    /**
     * @param type
     * @param code
     * @param description
     */
    public ConceptPropertyType(int type, String code, String description)
    {
        this.type = type;
        this.code = code;
        this.description = description;
    }
    
    public String toString()
    {
        return description;
    }
    
    public boolean equals(Object obj)
    {
        if (obj==null) return false;
        ConceptPropertyType propertyType = (ConceptPropertyType) obj;
        return type==propertyType.getType();
    }

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * @param typeDesc The description or code of the type
     * @return The concept property type or null if nothing was found to match.
     */
    public static ConceptPropertyType getType(String typeDesc)
    {
        for (int i=0;i<propertyTypes.length;i++)
        {
            if (propertyTypes[i].getDescription().equals(typeDesc)) return propertyTypes[i];
            if (propertyTypes[i].getCode().equals(typeDesc)) return propertyTypes[i];
        }
        return null;
    }   
}
