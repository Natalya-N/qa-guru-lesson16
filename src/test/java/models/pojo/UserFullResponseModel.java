package models.pojo;

public class UserFullResponseModel {
    DataResponseModel data;
    SupportResponseModel support;

    public SupportResponseModel getSupport() {
        return support;
    }

    public void setSupport(SupportResponseModel support) {
        this.support = support;
    }

    public DataResponseModel getData() {
        return data;
    }

    public void setData(DataResponseModel data) {
        this.data = data;
    }

}
