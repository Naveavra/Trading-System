export enum Action {

    addProduct = "add product", // manager, owner, creator 
    removeProduct = "remove product", //removes a product from store
    updateProduct = "update product", //updates product fields

    changeStoreDescription = "change store description", // manager, owner, creator //todo
    deletePurchasePolicy = "delete purchase policy", // manager, owner, creator //todo
    deleteDiscountPolicy = "delete discount policy", // manager, owner, creator //todo
    addPurchaseConstraint = "add purchase constraint", // manager, owner, creator //todo
    addDiscountConstraint = "add discount constraint", // manager, owner, creator //todo 

    viewMessages = "view messages", // manager, owner, creator //todo
    answerMessage = "view messages", // manager, owner, creator //todo
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

}