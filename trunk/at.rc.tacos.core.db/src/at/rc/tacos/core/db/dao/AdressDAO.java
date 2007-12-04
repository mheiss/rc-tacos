package at.rc.tacos.core.db.dao;

import java.sql.SQLException;

public interface AdressDAO 
{
    Integer addStreet(String streetname) throws SQLException;
    Integer getStreetID(String streetname) throws SQLException;
    Integer addStreetnumber(String streetnumber) throws SQLException;
    Integer getStreetnumberID(String streetnumber) throws SQLException;
    Integer addStreetStreetnumber(int streetID, int streetnumberID) throws SQLException;
    Integer getStreetStreetnumberID(int streetID, int streetnumberID) throws SQLException;
    Integer addCity(String cityname, int zipcode) throws SQLException;
    Integer getCityID(String cityname, int zipcode) throws SQLException;
    Integer addCityStreetStreetnumber(int cityID, int streetStreetnumberID) throws SQLException;
    Integer getCityStreetStreetnumberID(int cityID, int streetStreetnumberID) throws SQLException;
    
    //  List<Citys> listCitys() throws SQLException;
    //  List<Adresses> listAdresses() throws SQLException;
}
