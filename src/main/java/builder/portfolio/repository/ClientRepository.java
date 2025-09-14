package builder.portfolio.repository;

import builder.portfolio.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRepository {

    public String trackBudget(long projectId){
        String budgetStatus="";
        String sql="SELECT actual_spend, planned_budget FROM project WHERE project_id=?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            double actual_spend=0;
            double planned_budget=0;
            ps.setLong(1, projectId);


            ResultSet rs= ps.executeQuery();
            if(rs.next()){
                actual_spend=rs.getDouble("actual_spend");
                planned_budget=rs.getDouble("planned_subject");
            }
            if(actual_spend<planned_budget){
                budgetStatus= "IN BUDGET";
            }
            else{
                System.out.println("Out");
                budgetStatus= "OUT OF BUDGET";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budgetStatus;
    }
}
