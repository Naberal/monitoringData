import controller.ExcelController;
import controller.TimerController;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        new ExcelController(new File("/home/naberal/IdeaProjects/monitoringData/ПримерМСКонтрагентыSQLAzure2.xlsx"))
                .saveExcelData();
        new TimerController().getNewOrders();
    }
}
