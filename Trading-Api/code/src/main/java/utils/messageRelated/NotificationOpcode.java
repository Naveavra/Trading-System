package utils.messageRelated;

public enum NotificationOpcode {
        STORE_REVIEW(0), PRODUCT_REVIEW(1), PURCHASE_IN_STORE(2), QUESTION(3), CLOSE_STORE_PERMANENTLY(4), OPEN_STORE(5), CLOSE_STORE(6),
        APPOINT_MANAGER(7), APPOINT_OWNER(8), FIRE_MANAGER(9), FIRE_OWNER(10),
        ADD_MANGER_PERMISSTION(11), REMOVE_MANGER_PERMISSTION(12),
        COMPLAINT(13), CHAT_MESSAGE(14),
        REVIEW_FEEDBACK(15),
        CANCEL_MEMBERSHIP(16),
        PLACE_BID(17)
        ;


        NotificationOpcode(int i) {

        }
}


