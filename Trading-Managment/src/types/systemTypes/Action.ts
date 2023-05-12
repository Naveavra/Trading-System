export enum Action {

    addProduct = "add Product", // manager, owner, creator V
    removeProduct = "remove Product", //removes a product from store V
    updateProduct = "update Product", //updates product fields V

    changeStoreDescription = "change Store Description", // manager, owner, creator
    changePurchasePolicy = "change Purchase Policy", // manager, owner, creator
    changeDiscountPolicy = "change Discount Policy", // manager, owner, creator
    addPurchaseConstraint = "add Purchase Constraint", // manager, owner, creator
    addDiscountConstraint = "add Discount Constraint", // manager, owner, creator

    viewMessages = "view Messages", // manager, owner, creator
    answerMessage = "view Messages", // manager, owner, creator
    seeStoreHistory = "see Store History", // manager, owner, creator
    seeStoreOrders = "see Store Orders", // manager,owner,creator
    checkWorkersStatus = "check Workers Status", // manager, owner, creator
    appointManager = "appoint Manager", // owner, creator
    fireManager = "fire Manager", // owner, creator

    appointOwner = "appoint Owner", //owner, creator
    fireOwner = "fire Owner", // owner, creator
    changeManagerPermission = "change Manager Permission", // owner, creator

    closeStore = "close Store", // creator V
    reopenStore = "reopen Store", // creator V

}