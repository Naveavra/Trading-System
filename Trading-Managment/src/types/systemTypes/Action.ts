export enum Action {

    addProduct = "add product", // manager, owner, creator 
    removeProduct = "remove product", //removes a product from store
    updateProduct = "update product", //updates product fields

    changeStoreDetails = "change store details", // manager, owner, creator //todo
    changePurchasePolicy = "change purchase policy", // manager, owner, creator //todo
    changeDiscountPolicy = "change discount policy", // manager, owner, creator //todo
    addPurchaseConstraint = "add purchase constraint", // manager, owner, creator //todo
    addDiscountConstraint = "add discount constraint", // manager, owner, creator //todo 

    viewMessages = "view messages", // manager, owner, creator //todo
    answerMessage = "answer message", // manager, owner, creator //todo
    seeStoreHistory = "see store history", // manager, owner, creator //todo
    seeStoreOrders = "see store orders", // manager,owner,creator //todo
    checkWorkersStatus = "check workers status", // manager, owner, creator //todo
    appointManager = "appoint manager", // owner, creator //todo
    fireManager = "fire manager", // owner, creator//todo - done

    appointOwner = "appoint owner", //owner, creator //todo
    fireOwner = "fire owner", // owner, creator //todo
    changeManagerPermission = "change manager permission", // owner, creator  //todo

    closeStore = "close store", // creator //todo
    reopenStore = "reopen store", // creator //todo

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