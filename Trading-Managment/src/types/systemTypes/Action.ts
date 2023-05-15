export enum Action {

    addProduct = "add Product", // manager, owner, creator //todo : done
    removeProduct = "remove Product", //removes a product from store //todo : done
    updateProduct = "update Product", //updates product fields //todo : done

    changeStoreDescription = "change Store Description", // manager, owner, creator //todo : done
    changePurchasePolicy = "change Purchase Policy", // manager, owner, creator
    deleteDiscountPolicy = "delete Discount Policy", // manager, owner, creator
    addPurchaseConstraint = "add Purchase Constraint", // manager, owner, creator
    addDiscountConstraint = "add Discount Constraint", // manager, owner, creator

    viewMessages = "view Messages", // manager, owner, creator
    answerMessage = "view Messages", // manager, owner, creator
    seeStoreHistory = "see Store History", // manager, owner, creator
    seeStoreOrders = "see Store Orders", // manager,owner,creator
    checkWorkersStatus = "check Workers Status", // manager, owner, creator
    appointManager = "appoint Manager", // owner, creator
    fireManager = "fire Manager", // owner, creator //todo : done

    appointOwner = "appoint Owner", //owner, creator
    fireOwner = "fire Owner", // owner, creator //todo : done
    changeManagerPermission = "change Manager Permission", // owner, creator

    closeStore = "close Store", // creator
    reopenStore = "reopen Store", // creator

}