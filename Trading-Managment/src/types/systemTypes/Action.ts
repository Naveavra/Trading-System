export enum Action {

    addProduct = "add Product", // manager, owner, creator V
    removeProduct = "remove Product", //removes a product from store V
    updateProduct = "update Product", //updates product fields V

    changeStoreDescription = "change Store Description", // manager, owner, creator
    changePurchasePolicy = "change Purchase Policy", // manager, owner, creator
    changeDiscountPolicy = "change Discount Policy", // manager, owner, creator
    addPurchaseConstraint = "add Purchase Constraint", // manager, owner, creator
    addDiscountConstraint = "add Discount Constraint", // manager, owner, creator
    changeStoreDescription = "change Store Description", // manager, owner, creator
    changePurchasePolicy = "change Purchase Policy", // manager, owner, creator
    changeDiscountPolicy = "change Discount Policy", // manager, owner, creator
    addPurchaseConstraint = "add Purchase Constraint", // manager, owner, creator
    addDiscountConstraint = "add Discount Constraint", // manager, owner, creator

    viewMessages, // manager, owner, creator
    answerMessage, // manager, owner, creator
    seeStoreHistory, // manager, owner, creator
    seeStoreOrders, // manager,owner,creator
    checkWorkersStatus, // manager, owner, creator
    appointManager, // owner, creator
    fireManager, // owner, creator

    appointOwner, //owner, creator
    fireOwner, // owner, creator
    changeManagerPermission, // owner, creator

    closeStore, // creator
    reopenStore, // creator

}