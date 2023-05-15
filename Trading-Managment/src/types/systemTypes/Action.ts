export enum Action {

    addProduct = "add Product", // manager, owner, creator 
    removeProduct = "remove Product", //removes a product from store
    updateProduct = "update Product", //updates product fields

    changeStoreDescription = "change Store Description", // manager, owner, creator //todo
    deletePurchasePolicy = "delete Purchase Policy", // manager, owner, creator //todo
    deleteDiscountPolicy = "delete Discount Policy", // manager, owner, creator //todo
    addPurchaseConstraint = "add Purchase Constraint", // manager, owner, creator //todo
    addDiscountConstraint = "add Discount Constraint", // manager, owner, creator //todo 

    viewMessages = "view Messages", // manager, owner, creator //todo
    answerMessage = "view Messages", // manager, owner, creator //todo
    seeStoreHistory = "see Store History", // manager, owner, creator //todo
    seeStoreOrders = "see Store Orders", // manager,owner,creator //todo
    checkWorkersStatus = "check Workers Status", // manager, owner, creator //todo
    appointManager = "appoint Manager", // owner, creator //todo
    fireManager = "fire Manager", // owner, creator//todo - done

    appointOwner = "appoint Owner", //owner, creator //todo
    fireOwner = "fire Owner", // owner, creator //todo
    changeManagerPermission = "change Manager Permission", // owner, creator  //todo

    closeStore = "close Store", // creator //todo
    reopenStore = "reopen Store", // creator //todo

}