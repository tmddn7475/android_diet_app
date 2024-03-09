package kr.co.company.sw_team4.DB;

public class FoodListAdapterData {

    private String kcal;
    private String name;

    public void setName(String name){
        this.name = name;}
    public void setKcal(String kcal){
        this.kcal = kcal;}

    public String getKcal(){
        return this.kcal;}

    public String getName(){
        return this.name;}
}