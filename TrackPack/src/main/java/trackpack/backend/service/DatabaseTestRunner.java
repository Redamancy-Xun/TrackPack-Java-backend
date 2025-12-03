//package trackpack.backend.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import java.util.List;
//
//@Component
//public class DatabaseTestRunner implements CommandLineRunner {
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Override
//    public void run(String... args) throws Exception {
//        try {
//            Query query = entityManager.createNativeQuery("SELECT * FROM backpack LIMIT 1");
//            List resultList = query.getResultList();
//            if (resultList.isEmpty()) {
//                System.out.println("No records found in backpack table.");
//            } else {
//                System.out.println("Successfully connected to the backpack table.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Error querying backpack table: " + e.getMessage());
//        }
//    }
//}
