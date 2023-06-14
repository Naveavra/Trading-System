export enum Action {

    addProduct = "add product",
    removeProduct = "remove product",
    updateProduct = "update product",

    changeStoreDetails = "change store details",
    changePurchasePolicy = "change purchase policy",
    changeDiscountPolicy = "change discount policy",
    addPurchaseConstraint = "add purchase constraint",
    addDiscountConstraint = "add discount constraint",

    viewMessages = "view messages",
    answerMessage = "answer message",
    seeStoreHistory = "see store history",
    seeStoreOrders = "see store orders",
    checkWorkersStatus = "check workers status",
    appointManager = "appoint manager",
    fireManager = "fire manager",

    appointOwner = "appoint owner",
    fireOwner = "fire owner",
    changeManagerPermission = "change manager permission",

    closeStore = "close store",
    reopenStore = "reopen store",
    counterBid = "counter bid",
    answerBid = "answer bid",
    //admin
    closeStorePerminently = 'close store perminently',
    addAdmin = 'add admin',
    quit = 'quit',
    seeComplaints = 'see complaints',
    cancelMembership = 'cancel membership',
    watchMarketStatus = 'watch market status',
    updateServices = 'update services',
}
export const actionOrder = {
    [Action.addProduct]: 1,
    [Action.removeProduct]: 2,
    [Action.updateProduct]: 3,
    [Action.changeStoreDetails]: 4,
    [Action.changePurchasePolicy]: 5,
    [Action.changeDiscountPolicy]: 6,
    [Action.addPurchaseConstraint]: 7,
    [Action.addDiscountConstraint]: 8,
    [Action.viewMessages]: 9,
    [Action.answerMessage]: 10,
    [Action.seeStoreHistory]: 11,
    [Action.seeStoreOrders]: 12,
    [Action.checkWorkersStatus]: 13,
    [Action.appointManager]: 14,
    [Action.fireManager]: 15,
    [Action.appointOwner]: 16,
    [Action.fireOwner]: 17,
    [Action.changeManagerPermission]: 18,
    [Action.closeStore]: 19,
    [Action.reopenStore]: 20,
} as const;

export const ManagerActions: Action[] = [
    Action.addProduct,
    Action.removeProduct,
    Action.updateProduct,
    Action.changeStoreDetails,
    Action.changePurchasePolicy,
    Action.changeDiscountPolicy,
    Action.addPurchaseConstraint,
    Action.addDiscountConstraint,
    Action.viewMessages,
    Action.answerMessage,
    Action.seeStoreHistory,
    Action.seeStoreOrders,
    Action.checkWorkersStatus,
];