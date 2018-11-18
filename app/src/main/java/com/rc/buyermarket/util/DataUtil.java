package com.rc.buyermarket.util;

import android.content.Context;
import android.util.Log;

import com.rc.buyermarket.model.Country;
import com.rc.buyermarket.model.Exterior;
import com.rc.buyermarket.model.PropertyType;
import com.rc.buyermarket.model.SPModel;
import com.rc.buyermarket.model.SessionCountryWithAreaList;
import com.rc.buyermarket.model.SessionExteriorList;
import com.rc.buyermarket.model.SessionPropertyTypeList;
import com.rc.buyermarket.model.SessionStyleList;
import com.rc.buyermarket.model.Styles;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DataUtil {

    private static final String DEFAULT_COUNTRY_WITH_STATE_LIST = "{\"data\":[{\"id\":\"1\",\"country_name\":\"United States\",\"states\":[{\"id\":\"5\",\"name\":\"Alabama\",\"country_id\":\"1\"},{\"id\":\"6\",\"name\":\"Alaska\",\"country_id\":\"1\"},{\"id\":\"7\",\"name\":\"Arizona\",\"country_id\":\"1\"},{\"id\":\"8\",\"name\":\"Arkansas\",\"country_id\":\"1\"},{\"id\":\"9\",\"name\":\"California\",\"country_id\":\"1\"},{\"id\":\"10\",\"name\":\"Colorado\",\"country_id\":\"1\"},{\"id\":\"11\",\"name\":\"Connecticut\",\"country_id\":\"1\"},{\"id\":\"12\",\"name\":\"Delaware\",\"country_id\":\"1\"},{\"id\":\"13\",\"name\":\"Florida\",\"country_id\":\"1\"},{\"id\":\"14\",\"name\":\"Georgia\",\"country_id\":\"1\"},{\"id\":\"15\",\"name\":\"Hawaii\",\"country_id\":\"1\"},{\"id\":\"16\",\"name\":\"Idaho\",\"country_id\":\"1\"},{\"id\":\"17\",\"name\":\"Illinois\",\"country_id\":\"1\"},{\"id\":\"18\",\"name\":\"Indiana\",\"country_id\":\"1\"},{\"id\":\"19\",\"name\":\"Iowa\",\"country_id\":\"1\"},{\"id\":\"20\",\"name\":\"Kansas\",\"country_id\":\"1\"},{\"id\":\"21\",\"name\":\"Kentucky\",\"country_id\":\"1\"},{\"id\":\"22\",\"name\":\"Louisiana\",\"country_id\":\"1\"},{\"id\":\"23\",\"name\":\"Maine\",\"country_id\":\"1\"},{\"id\":\"24\",\"name\":\"Maryland\",\"country_id\":\"1\"},{\"id\":\"25\",\"name\":\"Massachusetts\",\"country_id\":\"1\"},{\"id\":\"26\",\"name\":\"Michigan\",\"country_id\":\"1\"},{\"id\":\"27\",\"name\":\"Minnesota\",\"country_id\":\"1\"},{\"id\":\"28\",\"name\":\"Mississippi\",\"country_id\":\"1\"},{\"id\":\"29\",\"name\":\"Missouri\",\"country_id\":\"1\"},{\"id\":\"30\",\"name\":\"Montana\",\"country_id\":\"1\"},{\"id\":\"31\",\"name\":\"Nebraska\",\"country_id\":\"1\"},{\"id\":\"32\",\"name\":\"Nevada\",\"country_id\":\"1\"},{\"id\":\"33\",\"name\":\"New Hampshire\",\"country_id\":\"1\"},{\"id\":\"34\",\"name\":\"New Jersey\",\"country_id\":\"1\"},{\"id\":\"35\",\"name\":\"New Mexico\",\"country_id\":\"1\"},{\"id\":\"36\",\"name\":\"New York\",\"country_id\":\"1\"},{\"id\":\"37\",\"name\":\"North Carolina\",\"country_id\":\"1\"},{\"id\":\"38\",\"name\":\"North Dakota\",\"country_id\":\"1\"},{\"id\":\"39\",\"name\":\"Ohio\",\"country_id\":\"1\"},{\"id\":\"40\",\"name\":\"Oklahoma\",\"country_id\":\"1\"},{\"id\":\"41\",\"name\":\"Oregon\",\"country_id\":\"1\"},{\"id\":\"42\",\"name\":\"Pennsylvania\",\"country_id\":\"1\"},{\"id\":\"43\",\"name\":\"Rhode Island\",\"country_id\":\"1\"},{\"id\":\"44\",\"name\":\"South Carolina\",\"country_id\":\"1\"},{\"id\":\"45\",\"name\":\"South Dakota\",\"country_id\":\"1\"},{\"id\":\"46\",\"name\":\"Tennessee\",\"country_id\":\"1\"},{\"id\":\"47\",\"name\":\"Texas\",\"country_id\":\"1\"},{\"id\":\"48\",\"name\":\"Utah\",\"country_id\":\"1\"},{\"id\":\"49\",\"name\":\"Vermont\",\"country_id\":\"1\"},{\"id\":\"50\",\"name\":\"Virginia\",\"country_id\":\"1\"},{\"id\":\"51\",\"name\":\"Washington\",\"country_id\":\"1\"},{\"id\":\"52\",\"name\":\"West Virginia\",\"country_id\":\"1\"},{\"id\":\"53\",\"name\":\"Wisconsin\",\"country_id\":\"1\"},{\"id\":\"54\",\"name\":\"Wyoming\",\"country_id\":\"1\"}]},{\"id\":\"2\",\"country_name\":\"Mexico\",\"states\":[]},{\"id\":\"3\",\"country_name\":\"Canda\",\"states\":[]}]}";
    private static final String DEFAULT_EXTERIOR_LIST = "{\"data\":[{\"id\":\"1\",\"exterior_key\":\"dont_matter\",\"exterior_value\":\"Dont Matter\"},{\"id\":\"2\",\"exterior_key\":\"brick\",\"exterior_value\":\"Brick\"},{\"id\":\"3\",\"exterior_key\":\"frame\",\"exterior_value\":\"Frame\"}]}";
    private static final String DEFAULT_STYLE_LIST = "{\"data\":[{\"id\":\"1\",\"style_key\":\"dont_matter\",\"style_vaue\":\"Dont Matter\"},{\"id\":\"2\",\"style_key\":\"bungalow\",\"style_vaue\":\"Bungalow\"},{\"id\":\"3\",\"style_key\":\"colonial\",\"style_vaue\":\"Colonial\"},{\"id\":\"4\",\"style_key\":\"cape_cod\",\"style_vaue\":\"Cape Cod\"},{\"id\":\"5\",\"style_key\":\"ranch\",\"style_vaue\":\"Ranch\"},{\"id\":\"6\",\"style_key\":\"victorian\",\"style_vaue\":\"Victorian\"}]}";
    private static final String DEFAULT_PROPERTY_TYPE_LIST = "{\"data\":[{\"id\":\"1\",\"property_key\":\"residential\",\"property_value\":\"Residential\"},{\"id\":\"2\",\"property_key\":\"condominium\",\"property_value\":\"Condominium\"}]}";

    /* Purchase type */
    public static List<SPModel> getAllPurchaseTypes() {
        List<SPModel> purchaseTypes = new ArrayList<>();

        purchaseTypes.add(new SPModel("", "Cash"));
        purchaseTypes.add(new SPModel("", "Mortgage"));
        purchaseTypes.add(new SPModel("", "Land Contract"));

        return purchaseTypes;
    }

    public static SPModel getPurchaseType(String purchaseTypeName) {
        List<SPModel> data = getAllPurchaseTypes();
        if (data != null && data.size() > 0) {
            for (SPModel spModel : data) {
                if (spModel.getSp_title().equalsIgnoreCase(purchaseTypeName)) {
                    return spModel;
                }
            }
        }
        return null;
    }

    /* Pre approved */
    public static List<SPModel> getAllPreApproved() {
        List<SPModel> preApprovedList = new ArrayList<>();

        preApprovedList.add(new SPModel("1", "Yes"));
        preApprovedList.add(new SPModel("0", "No"));

        return preApprovedList;
    }

    public static SPModel getPreApproved(String preApprovedKey) {
        List<SPModel> data = getAllPreApproved();
        if (data != null && data.size() > 0) {
            for (SPModel spModel : data) {
                if (spModel.getId().equalsIgnoreCase(preApprovedKey)) {
                    return spModel;
                }
            }
        }
        return null;
    }

    /* Bedroom */
    public static List<SPModel> getAllBedrooms() {
        List<SPModel> bedroomList = new ArrayList<>();

        bedroomList.add(new SPModel("1", "1+"));
        bedroomList.add(new SPModel("2", "2+"));
        bedroomList.add(new SPModel("3", "3+"));
        bedroomList.add(new SPModel("4", "4+"));
        bedroomList.add(new SPModel("5", "5+"));

        return bedroomList;
    }

    public static SPModel getBedroom(String bedroomKey) {
        List<SPModel> data = getAllBedrooms();
        if (data != null && data.size() > 0) {
            for (SPModel spModel : data) {
                if (spModel.getId().equalsIgnoreCase(bedroomKey)) {
                    return spModel;
                }
            }
        }
        return null;
    }

    /* Bathroom */
    public static List<SPModel> getAllBathrooms() {
        List<SPModel> bathroomList = new ArrayList<>();

        bathroomList.add(new SPModel("", "Don't Matter"));
        bathroomList.add(new SPModel("1", "1+"));
        bathroomList.add(new SPModel("2", "2+"));

        return bathroomList;
    }

    public static SPModel getBathroom(String bathroomKey) {
        List<SPModel> data = getAllBathrooms();
        if (data != null && data.size() > 0) {
            for (SPModel spModel : data) {
                if (spModel.getId().equalsIgnoreCase(bathroomKey)) {
                    return spModel;
                }
            }
        }
        return null;
    }

    /* Basement */
    public static List<SPModel> getAllBasements() {
        List<SPModel> basementList = new ArrayList<>();

        basementList.add(new SPModel("", "Don't Matter"));
        basementList.add(new SPModel("1", "Yes"));
        basementList.add(new SPModel("0", "No"));

        return basementList;
    }

    public static SPModel getBasement(String basementKey) {
        List<SPModel> data = getAllBasements();
        if (data != null && data.size() > 0) {
            for (SPModel spModel : data) {
                if (spModel.getId().equalsIgnoreCase(basementKey)) {
                    return spModel;
                }
            }
        }
        return null;
    }

    /* Garage */
    public static List<SPModel> getAllGarages() {
        List<SPModel> garageList = new ArrayList<>();

        garageList.add(new SPModel("", "Don't Matter"));
        garageList.add(new SPModel("1", "Yes"));
        garageList.add(new SPModel("0", "No"));

        return garageList;
    }

    public static SPModel getGarage(String garageKey) {
        List<SPModel> data = getAllGarages();
        if (data != null && data.size() > 0) {
            for (SPModel spModel : data) {
                if (spModel.getId().equalsIgnoreCase(garageKey)) {
                    return spModel;
                }
            }
        }
        return null;
    }

    /* Country with state */
    public static List<Country> getAllCountryWithStates(Context context) {
        String sessionData = AppPref.getPreferences(context, AllConstants.SESSION_CITY_WITH_AREA_LIST);
        List<Country> offlineData = new ArrayList<>();

        if (AppUtil.isNullOrEmpty(sessionData)) {
            sessionData = DataUtil.DEFAULT_COUNTRY_WITH_STATE_LIST;
            Log.d("SessionData: ", "getAllCountryWithState(default): " + offlineData);
        }
        offlineData = SessionCountryWithAreaList.getResponseObject(sessionData, SessionCountryWithAreaList.class).getData();

        return offlineData;
    }

    /* Exterior */
    public static List<Exterior> getAllExteriors(Context context) {
        String sessionData = AppPref.getPreferences(context, AllConstants.SESSION_EXTERIOR_LIST);
        List<Exterior> offlineData = new ArrayList<>();

        if (AppUtil.isNullOrEmpty(sessionData)) {
            sessionData = DataUtil.DEFAULT_EXTERIOR_LIST;
            Log.d("SessionData: ", "getAllExteriors(default): " + offlineData);
        }
        offlineData = SessionExteriorList.getResponseObject(sessionData, SessionExteriorList.class).getData();

        return offlineData;
    }

    public static Exterior getExterior(Context context, String exteriorKey) {
        List<Exterior> data = getAllExteriors(context);
        if (data != null && data.size() > 0) {
            for (Exterior propertyType : data) {
                if (propertyType.getExterior_key().equalsIgnoreCase(exteriorKey)) {
                    return propertyType;
                }
            }
        }
        return null;
    }

    /* Property Type */
    public static List<PropertyType> getAllPropertyTypes(Context context) {
        String sessionData = AppPref.getPreferences(context, AllConstants.SESSION_PROPERTY_TYPE_LIST);
        List<PropertyType> offlineData = new ArrayList<>();

        if (AppUtil.isNullOrEmpty(sessionData)) {
            sessionData = DataUtil.DEFAULT_PROPERTY_TYPE_LIST;
            Log.d("SessionData: ", "getAllPropertyTypes(default): " + offlineData);
        }
        offlineData = SessionPropertyTypeList.getResponseObject(sessionData, SessionPropertyTypeList.class).getData();

        return offlineData;
    }

    public static PropertyType getPropertyType(Context context, String propertyTypeKey) {
        List<PropertyType> data = getAllPropertyTypes(context);
        if (data != null && data.size() > 0) {
            for (PropertyType propertyType : data) {
                if (propertyType.getProperty_key().equalsIgnoreCase(propertyTypeKey)) {
                    return propertyType;
                }
            }
        }
        return null;
    }

    /* Styles */
    public static List<Styles> getAllStyles(Context context) {
        String sessionData = AppPref.getPreferences(context, AllConstants.SESSION_STYLE_LIST);
        List<Styles> offlineData = new ArrayList<>();

        if (AppUtil.isNullOrEmpty(sessionData)) {
            sessionData = DataUtil.DEFAULT_STYLE_LIST;
            Log.d("SessionData: ", "getAllStyles(default): " + offlineData);
        }
        offlineData = SessionStyleList.getResponseObject(sessionData, SessionStyleList.class).getData();

        return offlineData;
    }

    public static Styles getStyle(Context context, String styleKey) {
        List<Styles> data = getAllStyles(context);
        if (data != null && data.size() > 0) {
            for (Styles styles : data) {
                if (styles.getStyle_key().equalsIgnoreCase(styleKey)) {
                    return styles;
                }
            }
        }
        return null;
    }
}