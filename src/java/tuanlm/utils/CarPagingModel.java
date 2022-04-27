/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.utils;

import java.util.ArrayList;
import java.util.List;
import tuanlm.dto.CarsDTO;

/**
 *
 * @author MINH TUAN
 */
public class CarPagingModel {
    public final static int NUMRECORDOFEACHPAGE = 3;
    private int totalPage = 1;
    private List pagingList;
    
    public int getNUMRECORDOFEACHPAGE() {
        return NUMRECORDOFEACHPAGE;
    }
    
    public int getTotalPage(List<CarsDTO> carList) {
        if(carList == null || carList.size() <= NUMRECORDOFEACHPAGE) {
            return 1;
        }
        if(carList.size() % NUMRECORDOFEACHPAGE == 0) {
            totalPage = carList.size() / NUMRECORDOFEACHPAGE;
        }
        else {
            totalPage = carList.size() / NUMRECORDOFEACHPAGE + 1;
        }
        return totalPage;
    }
    
    public List<CarsDTO> loadPaging(List<CarsDTO> carList, int curPage) {
        if(carList == null || carList.size() <= NUMRECORDOFEACHPAGE) {
            return carList;
        }
        if(pagingList == null) {
            pagingList = new ArrayList<>();
        }

        int sizeTotal = carList.size();
        int startRecord = (curPage - 1) * NUMRECORDOFEACHPAGE + 1;
        int endRecord = startRecord + NUMRECORDOFEACHPAGE - 1;
        if(sizeTotal < endRecord) {
            endRecord = sizeTotal;
        }
        for (int count = 0; count < carList.size(); count ++) {
            if(count + 1 >= startRecord && count + 1 <= endRecord) {
                pagingList.add(carList.get(count));
            }
        }

        return pagingList;
    }
}
