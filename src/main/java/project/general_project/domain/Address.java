package project.general_project.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {
    private String zipcode; //우편번호
    private String city; //시, 도
    private String detailAddress;

    private Address(String zipcode, String city, String detailAddress) {
        this.zipcode = zipcode;
        this.city = city;
        this.detailAddress = detailAddress;
    }

    public Address() {

    }

    public static Address createAddress(String zipcode, String city, String detailAddress){
        Address address=new Address(zipcode,city,detailAddress);
        return address;
    }
}
