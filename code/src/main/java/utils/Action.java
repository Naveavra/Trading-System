package utils;

public enum Action {

    addProduct, // manager, owner, creator
    removeProduct, //removes a product from store
    updateProduct, //updates product fields

    changeStoreDescription, // manager, owner, creator
    changePurchasePolicy, // manager, owner, creator
    changeDiscountPolicy, // manager, owner, creator
    addPurchaseConstraint, // manager, owner, creator
    addDiscountConstraint, // manager, owner, creator

    viewMessages, // manager, owner, creator
    answerMessage, // manager, owner, creator
    seeStoreHistory, // manager, owner, creator
    seeStoreOrders, // manager,owner,creator
    checkWorkersStatus, // manager, owner, creator
    appointManager, // manager, owner, creator
    fireManager, // manager, owner, creator

    appointOwner, //owner, creator
    fireOwner, // owner, creator
    changeManagerPermission, // owner, creator

    closeStore, // creator
    reopenStore, // creator

}



