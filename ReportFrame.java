package jdbc;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ReportFrame extends JFrame {
    public ReportFrame() {
        // Frame properties
        setTitle("Reports and Analytics");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create the chart
        JFreeChart barChart = createChart();
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(760, 520));
        setContentPane(chartPanel);

        setVisible(true);
    }

    private JFreeChart createChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ems", "root", "bhargav@143");
            Statement stmt = con.createStatement();
            String query = "SELECT department, COUNT(*) AS employee_count FROM employees GROUP BY department";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String department = rs.getString("department");
                int employeeCount = rs.getInt("employee_count");
                dataset.addValue(employeeCount, "Employee Count", department);
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ChartFactory.createBarChart(
                "Employee Count by Department",  // Chart title
                "Department",                   // Category axis label
                "Employee Count",               // Value axis label
                dataset,                        // Dataset
                PlotOrientation.VERTICAL,       // Orientation
                true,                           // Include legend
                true,                           // Tooltips
                false                           // URLs
        );
    }

    public static void main(String[] args) {
        new ReportFrame();
    }
}
