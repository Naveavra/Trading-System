package database.dtos;


import domain.store.discount.Discount;
import domain.store.purchase.PurchasePolicy;
import domain.store.storeManagement.AppHistory;
import jakarta.persistence.*;
import utils.Pair;
import utils.infoRelated.Info;
import utils.infoRelated.ProductInfo;
import utils.messageRelated.Question;
import utils.messageRelated.StoreReview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "stores")
public class StoreDto {

    @Id
    private int storeId;
    private String name;
    private boolean isActive;
    private String image;

    private String description;
    @ManyToOne
    @JoinColumn(name = "creatorId", foreignKey = @ForeignKey(name = "creatorId"), referencedColumnName = "id")
    private MemberDto memberDto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="storeDto")
    private List<InventoryDto> inventoryDtos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="storeDto")
    private List<RoleDto> roles;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="storeDto")
    private List<AppointmentDto> appointments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="storeDto")
    private List<StoreReviewDto> storeReviews;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="storeDto")
    private List<QuestionDto> questions;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="storeDto")
    private List<DicountDto> dicountDtos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="storeDto")
    private List<ConstraintDto> constraints;


    public StoreDto(){

    }
    public StoreDto(MemberDto memberDto,int storeId, String name,String description,  String image){
        this.storeId = storeId;
        this.name = name;
        this.isActive = true;
        this.image = image;
        this.memberDto = memberDto;
        this.description = description;
    }

    public int getId(){return storeId;}

    public void  setId(int storeId){this.storeId = storeId;}

    public String getName(){return this.name;}

    public void setName(String name){this.name = name;}

    public boolean isActive(){return this.isActive;}
    public void setActive(boolean state){this.isActive = state;}

    public String getImage(){return this.image;}

    public void setImage(String image){this.image = image;}

    public MemberDto getCreatorId(){return memberDto;}

    public String getDescription(){return this.description;}

    public void setDescription(String desc){this.description = desc;}

    public List<InventoryDto> getStoreProducts(){
        return inventoryDtos;
    }

    public void setInventory(List<ProductInfo> products) {
        List<InventoryDto> inventoryDtos = new ArrayList<>();
        for (ProductInfo product : products) {
            InventoryDto inventoryDto = new InventoryDto(this, product.getId(), product.getQuantity(), product.name,
                    product.description, product.getPrice(), product.getImg());
            inventoryDto.setCategory(product.getCategories());
            inventoryDto.setReviews(product.getReviews());
            inventoryDtos.add(inventoryDto);
        }

        this.inventoryDtos = inventoryDtos;
    }

    public List<StoreReviewDto> getStoreReviews(){
        return storeReviews;
    }


    public void setStoreConstraints(List<PurchasePolicy> constraints) {
        List<ConstraintDto> ans = new ArrayList<>();
        for (PurchasePolicy policy : constraints)
            ans.add(new ConstraintDto(this, policy.getId(), policy.getContent()));
        this.constraints = ans;
    }
    public List<ConstraintDto> getStoreConstraints(){
        return constraints;
    }

    public void setStoreReviews(List<StoreReview> reviews) {
        List<StoreReviewDto> ans = new ArrayList<>();
        for (StoreReview message : reviews)
            ans.add(new StoreReviewDto(this, message.getMessageId(), message.getSender().getId(), message.getContent(), message.getRating(),
                    message.getOrderId(), message.getSeen()));
        this.storeReviews = ans;
    }

    public List<DicountDto> getDicountDtos() {
        return dicountDtos;
    }
    public void setStoreDiscounts(List<Discount> discounts)
    {
        List<DicountDto> ans = new ArrayList<>();
        for (Discount di : discounts)
        {
            ans.add(new DicountDto(this, di.getDiscountID(), di.getContent()));
        }
        this.dicountDtos = ans;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public List<AppointmentDto> getAppointments() {
        return appointments;
    }
    public void setRoles(AppHistory appHistory) {
        List<RoleDto> roleDtos = new ArrayList<>();
        Set<Integer> checkDup = new HashSet<>();
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for(Pair<Info, List<Info>> p : appHistory.getAppHistory()){
            if(!checkDup.contains(p.getFirst().getId())) {
                RoleDto roleDto = new RoleDto(this, p.getFirst().getId(), p.getFirst().getRole().toString(), p.getFirst().getEmail());
                roleDtos.add(roleDto);
                roleDto.setPermissions(p.getFirst().getActions());
                checkDup.add(p.getFirst().getId());
            }
            for(Info info : p.getSecond()) {
                if(!checkDup.contains(info.getId())) {
                    RoleDto roleDto = new RoleDto(this, info.getId(), info.getRole().toString(), info.getEmail());
                    roleDtos.add(roleDto);
                    roleDto.setPermissions(info.getActions());
                    checkDup.add(info.getId());
                }
                appointmentDtos.add(new AppointmentDto(0, p.getFirst().getId(), info.getId()));
            }
        }
        roles = roleDtos;
        appointments = appointmentDtos;

    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        List<QuestionDto> ans = new ArrayList<>();
        for (Question message : questions)
            ans.add(new QuestionDto(this, message.getMessageId(), message.getSender().getId(), message.getContent(), message.isGotFeedback(),
                     message.getSeen()));
        this.questions = ans;
    }
}
