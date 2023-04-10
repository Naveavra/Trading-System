package utils;

import java.util.concurrent.ConcurrentLinkedDeque;

public enum Action {
    buyProduct, //buyer, seller, manager, owner, creator
    sellProduct, //seller, manager, owner, creator
    appointManager, // manager, owner, creator
    createStore, //buyer, seller, manager, owner, creator
    addProduct, // manager, owner, creator
    changeStoreDescription, // manager, owner, creator
    getProductInformation, //buyer, seller, manager, owner, creator
    getStoreInformation, //buyer, seller, manager, owner, creator
    writeReview, //buyer, seller, manager, owner, creator
    rateProduct, //buyer, seller, manager, owner, creator
    rateStore, //buyer, seller, manager, owner, creator
    sendQuestion, //buyer, seller, manager, owner, creator
    sendComplaint, //buyer, seller, manager, owner, creator
    changePurchasePolicy, // manager, owner, creator
    changeDiscountPolicy, // manager, owner, creator
    addPurchaseConstraint, // manager, owner, creator
    fireManager, // manager, owner, creator
    addOwner, // owner, creator
    fireOwner, // owner, creator
    changeManagerPermission, // owner, creator
    closeStore, // creator
    reopenStore, // creator
    checkWorkersStatus, // manager, owner, creator
    viewQuestions, // manager, owner, creator
    answerQuestions, // manager, owner, creator
    seeStoreHistory, // manager, owner, creator

}

