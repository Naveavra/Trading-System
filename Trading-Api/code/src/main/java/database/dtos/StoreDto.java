package database.dtos;

import jakarta.persistence.*;


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
}
