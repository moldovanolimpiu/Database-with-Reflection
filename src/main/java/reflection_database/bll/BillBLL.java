package reflection_database.bll;

import reflection_database.bll.validators.Validator;
import reflection_database.dao.BillDAO;
import reflection_database.model.Bill;

import java.util.List;


public class BillBLL {
    private List<Validator<Bill>> validators;
    private BillDAO billDAO;
    public BillBLL() {
        billDAO = new BillDAO();
    }

    public List<Bill> findAllBills() {
        List<Bill> bills = billDAO.findAll();
        if(bills == null){
            System.out.println("Bill list is empty");
            return null;
        }
        return bills;
    }

    public void insertBill(Bill bill) {
        System.out.println("Inserting bill");
        billDAO.insert(bill);
    }

    public int getMaxOrderId(){

        return billDAO.returnMaxOrderId();
    }
}
