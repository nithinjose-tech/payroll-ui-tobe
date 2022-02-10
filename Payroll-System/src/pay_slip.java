import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;  
import java.util.Date;  

public class pay_slip extends JFrame implements ActionListener{

    Choice c1;
    JTextArea t1;
    JButton b1;

    pay_slip(){
        
        setSize(800,700);
        setLocation(400,150);
        c1 = new Choice();
        try{
            conn c = new conn();
            ResultSet rs = c.s.executeQuery("select * from employee");
            while(rs.next()){
                c1.add(rs.getString("id"));
            }
        }catch(Exception e) { }
    
        setLayout(new BorderLayout());
        
        JPanel p1 = new JPanel();
        p1.add(new JLabel("Select Id"));
        p1.add(c1);
        add(p1,"North");
        // c1.addItemListener(this);
    
        t1 = new JTextArea(30,80);
        JScrollPane jsp = new JScrollPane(t1);
     
        Font f1 = new Font("arial",Font.BOLD,20);
        t1.setFont(f1);
        
        b1 = new JButton("Generate Pay Slip");
    
        add(b1,"South");
        add(jsp,"Center");
        b1.addActionListener(this);
    
    }
   
    public void actionPerformed(ActionEvent e) {
     
        try{
            conn c = new conn();
         
            ResultSet rs = c.s.executeQuery("select * from employee join post_table on employee.post_company=post_table.post where id="+c1.getSelectedItem());
            rs.next();
            String name = rs.getString("name");
            double min_salary = rs.getDouble("min_base");
            double max_salary = rs.getDouble("max_base");
            String join_date = rs.getString("join_date") ;     
            double medical = rs.getDouble("med");
            
            rs.close();
            
            rs = c.s.executeQuery("select * from pf_da");
            rs.next();
            int pf_percent = rs.getInt("pf");
            int da_percent = rs.getInt("da");
            
            
         
            rs = c.s.executeQuery("select * from employee join hra_table on employee.city=hra_table.city where id="+c1.getSelectedItem());
            double gross=0;
            double net=0;
            
 
            java.util.Date d1 = new java.util.Date();
            int month = d1.getMonth();
            t1.setText(" ----------------   PAY SLIP FOR THE MONTH OF "+month+" ,2019  ------------------------");
            t1.append("\n");
  
            if(rs.next()){
                
                int hra_percent = rs.getInt("percent");
          
                t1.append("\n     Employee ID "+rs.getString("id"));
                t1.append("\n     Employee Name "+name);
 
                t1.append("\n----------------------------------------------------------------");
                t1.append("\n");
                
                System.out.println(name);
                System.out.println(min_salary);
                System.out.println(max_salary);
                System.out.println(min_salary);
                System.out.println(join_date);
                System.out.println(medical);
                System.out.println(pf_percent);
                System.out.println(da_percent);
                System.out.println(hra_percent);
                
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
                Date today = new Date();
                System.out.println(formatter.format(today));
//                formatter.setLenient(false);
                Date joining_date = formatter.parse(join_date);
                System.out.println(today);
                
//                Date joining_date=new SimpleDateFormat("yyyy/MM/dd").parse(join_date);
                System.out.println("Joining:"+joining_date.getTime());
                    System.out.println(join_date.getClass().getName());
                System.out.println("Todays:"+today.getTime());
                
                long difference_In_Time = today.getTime() - joining_date.getTime();
                long experience= (difference_In_Time / (1000l * 60 * 60 * 24 * 365));
                
                System.out.println("Experience: "+experience);
                
                double basic=0;
                
                if(experience<2)
                {
                    basic = min_salary; 
                }else if(experience>=2){
                     basic = min_salary+min_salary*((float)experience/100);
                     System.out.println("Increment salary: "+basic);
                     System.out.println(min_salary+min_salary*(experience/100));
                     
                }
                
                if(basic>max_salary)
                {
                    basic = max_salary;
                }
                
                

                double hra = hra_percent*(basic/100);
                t1.append("\n                  HRA         : "+hra);
                double da = da_percent*(basic/100);
                t1.append("\n                  DA          : "+da);
//                double med  = rs.getDouble("med");
                t1.append("\n                  MED         : "+medical);
                double pf  = pf_percent*(basic/100);
                t1.append("\n                  PF          : "+pf);
//                double basic = rs.getDouble("basic_salary");
                gross = hra+da+medical+pf+basic;
                net = gross - pf;
                t1.append("\n                  BASIC SALARY : "+basic);

                t1.append("\n-------------------------------------------------------");
                t1.append("\n");
 
                t1.append("\n       GROSS SALARY :"+gross+"    \n       NET SALARY : "+net);
                t1.append("\n       Tax   :   2.1% of gross "+ (gross*2.1/100));   
                t1.append("\n -------------------------------------------------");
                t1.append("\n");
                t1.append("\n");    
                t1.append("\n");
                t1.append("   (  Signature  )      ");

            }
        }catch(Exception ee) {
            ee.printStackTrace();
        }
 
   
    }
    public static void main(String[] args){
        new pay_slip().setVisible(true);
    }
}
