package database.daos;

import database.DbEntity;
import database.HibernateUtil;
import domain.store.storeManagement.Store;
import org.hibernate.Session;
import utils.messageRelated.*;

import java.util.*;

public class MessageDao {

    private static HashMap<Integer, Complaint> complaintMap = new HashMap<>();
    private static boolean complaints = false;
    private static HashMap<Integer, HashMap<Integer, StoreReview>> storeReviewMap = new HashMap<>();
    private static Set<Integer> storeReviews = new HashSet<>();
    private static HashMap<Integer, HashMap<Integer, HashMap<Integer, ProductReview>>> productReviewMap = new HashMap<>();
    private static HashMap<Integer, Set<Integer>> productReviews = new HashMap<>();
    private static HashMap<Integer, HashMap<Integer, Question>> questionMap = new HashMap<>();
    private static Set<Integer> questions = new HashSet<>();


    public static void saveMessage(Message m, Session session) throws Exception{
        Dao.save(m, session);
    }

    public static int getMaxId(){
        return Dao.getMaxId("Message", "messageId");
    }
    public static Complaint getComplaint(int messageId) throws Exception{
        if(complaintMap.containsKey(messageId))
            return complaintMap.get(messageId);
        Complaint complaint = (Complaint) Dao.getById(Complaint.class, messageId);
        if(complaint != null) {
            complaintMap.put(complaint.getMessageId(), complaint);
            complaint.initialParams();
        }
        return complaint;
    }

    public static List<Complaint> getComplaints() throws Exception{
        if(!complaints) {
            List<? extends DbEntity> complaintsDto = Dao.getAllInTable("Complaint");
            for (Complaint complaint : (List<Complaint>) complaintsDto)
                if(!complaintMap.containsKey(complaint.getMessageId())) {
                    complaintMap.put(complaint.getMessageId(), complaint);
                    complaint.initialParams();
                }
            complaints = true;
        }
        return new ArrayList<>(complaintMap.values());
    }

    public static void removeComplaint(int messageId, Session session) throws Exception{
        Dao.removeIf("Complaint", String.format("messageId = %d", messageId), session);
        complaintMap.remove(messageId);
    }

    public static StoreReview getStoreReview(int storeId, int messageId) throws Exception{
        if(storeReviewMap.containsKey(storeId))
            if(storeReviewMap.get(storeId).containsKey(messageId))
                return storeReviewMap.get(storeId).get(messageId);

        StoreReview review = (StoreReview) Dao.getById(StoreReview.class, messageId);
        if(review != null) {
            if(storeReviewMap.containsKey(storeId))
                storeReviewMap.put(storeId, new HashMap<>());
            storeReviewMap.get(storeId).put(review.getMessageId(), review);
            review.initialParams();
        }
        return review;
    }

    public static List<StoreReview> getStoreReviews(int storeId) throws Exception{
        if(!storeReviews.contains(storeId)) {
            if (!storeReviewMap.containsKey(storeId))
                storeReviewMap.put(storeId, new HashMap<>());

            List<? extends DbEntity> reviewsDto = Dao.getListById(StoreReview.class, storeId, "StoreReview", "storeId");
            for (StoreReview review : (List<StoreReview>) reviewsDto) {
                if(!(review instanceof ProductReview)) {
                    if (!storeReviewMap.get(storeId).containsKey(review.getMessageId())) {
                        storeReviewMap.get(storeId).put(review.getMessageId(), review);
                        review.initialParams();
                    }
                }
            }
            storeReviews.add(storeId);
        }
        return new ArrayList<>(storeReviewMap.get(storeId).values());
    }

    public static void removeStoreReview(int storeId, int messageId, Session session) throws Exception{
        Dao.removeIf("StoreReview", String.format("messageId = %d", messageId), session);
        if(storeReviewMap.containsKey(storeId))
            storeReviewMap.get(storeId).remove(messageId);
    }

    public static ProductReview getProductReview(int storeId, int productId, int messageId) throws Exception{
        if(productReviewMap.containsKey(storeId))
            if(productReviewMap.get(storeId).containsKey(productId))
                if(productReviewMap.get(storeId).get(productId).containsKey(messageId))
                    productReviewMap.get(storeId).get(productId).get(messageId);

        ProductReview review = (ProductReview) Dao.getById(ProductReview.class, messageId);
        if(review != null) {
            if(productReviewMap.containsKey(storeId))
                productReviewMap.put(storeId, new HashMap<>());
            if(productReviewMap.get(storeId).containsKey(productId))
                productReviewMap.get(storeId).put(productId, new HashMap<>());

            productReviewMap.get(storeId).get(productId).put(review.getMessageId(), review);
            review.initialParams();
        }
        return review;
    }

    public static List<ProductReview> getProductReviews(int storeId, int productId) throws Exception{
        if(!productReviews.containsKey(storeId))
            productReviews.put(storeId, new HashSet<>());

        if(!productReviews.get(storeId).contains(productId)) {
            if (!productReviewMap.containsKey(storeId))
                productReviewMap.put(storeId, new HashMap<>());
            if(!productReviewMap.get(storeId).containsKey(productId))
                productReviewMap.get(storeId).put(productId, new HashMap<>());

            List<? extends DbEntity> reviewDto = Dao.getListByCompositeKey(ProductReview.class, storeId, productId,
                    "ProductReview", "storeId", "productId");
            for (ProductReview review : (List<ProductReview>) reviewDto) {
                if(!productReviewMap.get(storeId).get(productId).containsKey(review.getMessageId())) {
                    productReviewMap.get(storeId).get(productId).put(review.getMessageId(), review);
                    review.initialParams();
                }
            }
            productReviews.get(storeId).add(productId);
        }
        return new ArrayList<>(productReviewMap.get(storeId).get(productId).values());
    }

    public static void removeProductReview(int storeId, int productId, int messageId, Session session) throws Exception{
        Dao.removeIf("ProductReview", String.format("messageId = %d", messageId), session);

        if(!productReviewMap.containsKey(storeId))
            productReviewMap.put(storeId, new HashMap<>());
        if(!productReviewMap.get(storeId).containsKey(productId))
            productReviewMap.get(storeId).put(productId, new HashMap<>());
        productReviewMap.get(storeId).get(productId).remove(messageId);

    }

    public static Question getQuestion(int storeId, int messageId) throws Exception{
        if(questionMap.containsKey(storeId))
            if(questionMap.get(storeId).containsKey(messageId))
                questionMap.get(storeId).get(messageId);

        Question question = (Question) Dao.getById(Question.class, messageId);
        if(question != null) {
            if(!questionMap.containsKey(storeId))
                questionMap.put(storeId, new HashMap<>());
            questionMap.get(storeId).put(question.getMessageId(), question);
            question.initialParams();
        }
        return question;
    }

    public static List<Question> getQuestions(int storeId) throws Exception{
        if(!questions.contains(storeId)) {
            if(!questionMap.containsKey(storeId))
                questionMap.put(storeId, new HashMap<>());
            List<? extends DbEntity> questionDto = Dao.getListById(Question.class, storeId, "Question",
                    "storeId");
            for (Question question : (List<Question>) questionDto)
                if(!questionMap.get(storeId).containsKey(question.getMessageId())) {
                    questionMap.get(storeId).put(question.getMessageId(), question);
                    question.initialParams();
                }

            questions.add(storeId);
        }
        return new ArrayList<>(questionMap.get(storeId).values());
    }

    public static void removeQuestion(int messageId, Session session) throws Exception{
        Dao.removeIf("Question", String.format("messageId = %d", messageId), session);
        questionMap.remove(messageId);
    }

    public static void removeStoreMessages(int storeId, Session session) throws Exception{
        Dao.removeIf("StoreReview", String.format("storeId = %d", storeId), session);
        storeReviews.remove(storeId);
        storeReviewMap.remove(storeId);
        Dao.removeIf("ProductReview", String.format("storeId = %d", storeId), session);
        productReviews.remove(storeId);
        productReviewMap.remove(storeId);
        Dao.removeIf("Question", String.format("storeId = %d", storeId), session);
        questions.remove(storeId);
        questionMap.remove(storeId);
    }

    public static void removeMemberMessage(int userId, Session session) throws Exception{
        Dao.removeIf("StoreReview", String.format("senderId = %d", userId), session);
        List<StoreReview> removeSReview = new ArrayList<>();
        for(HashMap<Integer, StoreReview> reviews : storeReviewMap.values())
            for(StoreReview review : reviews.values())
                if(review.getSenderId() == userId)
                    removeSReview.add(review);
        for(StoreReview review : removeSReview)
            storeReviewMap.get(review.getStoreId()).remove(review.getMessageId());

        List<ProductReview> removePReview = new ArrayList<>();
        Dao.removeIf("ProductReview", String.format("senderId = %d", userId), session);
        for(HashMap<Integer, HashMap<Integer, ProductReview>> tmp : productReviewMap.values())
            for(HashMap<Integer, ProductReview> reviews : tmp.values())
                for(ProductReview review : reviews.values())
                    if(review.getSenderId() == userId)
                        removePReview.add(review);
        for(ProductReview review : removePReview)
            productReviewMap.get(review.getStoreId()).get(review.getProductId()).remove(review.getMessageId());

        List<Question> removeQ = new ArrayList<>();
        Dao.removeIf("Question", String.format("senderId = %d", userId), session);
        for(HashMap<Integer, Question> questions : questionMap.values())
            for(Question question : questions.values())
                if(question.getSenderId() == userId)
                    removeQ.add(question);
        for(Question q : removeQ)
            questionMap.get(q.getStoreId()).remove(q.getMessageId());
    }
}
