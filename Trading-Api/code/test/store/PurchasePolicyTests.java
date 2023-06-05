package store;
import domain.store.discount.AbstractDiscount;
import domain.store.discount.DiscountFactory;
import domain.store.discount.compositeDiscountTypes.LogicalDiscountComposite;
import domain.store.discount.compositeDiscountTypes.NumericDiscountComposite;
import domain.store.discount.discountDataObjects.CompositeDataObject;
import domain.store.discount.discountDataObjects.DiscountDataObject;
import domain.store.discount.discountDataObjects.PredicateDataObject;
import domain.store.discount.predicates.DiscountPredicate;
import domain.store.order.OrderController;
import domain.store.product.Inventory;

import static domain.store.discount.predicates.DiscountPredicate.composore.*;
import static org.junit.jupiter.api.Assertions.*;

import domain.store.storeManagement.Store;
import domain.store.storeManagement.StoreController;
import domain.user.Member;
import domain.user.ShoppingCart;
import org.junit.jupiter.api.*;
import utils.infoRelated.ProductInfo;
import utils.orderRelated.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class PurchasePolicyTests {
}
