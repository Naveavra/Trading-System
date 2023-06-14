package utils.messageRelated;

public enum NotificationOpcode {
//        STORE_REVIEW(0),//getStore
//        PRODUCT_REVIEW(1),//getStore
//        PURCHASE_IN_STORE(2),//getStore
//        QUESTION(3),//getStore
//        CLOSE_STORE_PERMANENTLY(4),//getStore,getClientData
//        OPEN_STORE(5),//getClientData
//        CLOSE_STORE(6),//getStore,getClientData
//        APPOINT_MANAGER(7),//getStore
//        APPOINT_OWNER(8),//getStore
//        FIRE_MANAGER(9),//getStore
//        FIRE_OWNER(10),//getStore
//        ADD_MANGER_PERMISSTION(11),//getStore
//        REMOVE_MANGER_PERMISSTION(12),//getStore
//        COMPLAINT(13),//getAdminData
//        CHAT_MESSAGE(14),//getClientData
//        REVIEW_FEEDBACK(15),//getStore,getComplains
//        CANCEL_MEMBERSHIP(16),//CANCEL_MEMBERSHIP
//        PLACE_BID(17),//getStore
        GET_CLIENT_DATA,
        GET_STORE_DATA,
        GET_ADMIN_DATA,
        GET_COMPLAINS,
        CANCEL_MEMBERSHIP,
        GET_CLIENT_DATA_AND_STORE_DATA,
        GET_CLIENT_DATA_AND_COMPLAINS,
        GET_STORE_DATA_AND_COMPLAINS,
        ;

}


