package com.korea.dao;

import com.korea.dto.MemberDTO;

import java.sql.*;

public class MemberDAO
{
    private String url = "jdbc:mysql://localhost:3306/board";
    private String id = "root";
    private String pw = "1234";

    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    private static MemberDAO instance;

    public static MemberDAO getInstance()
    {
        if(instance == null)
        {
            instance = new MemberDAO();
        }
        return instance;
    }

    private MemberDAO()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, id, pw);
            System.out.println("DB Connected.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // INSERT 함수
    public boolean insert(MemberDTO dto)
    {
        try
        {
            pstmt = conn.prepareStatement("insert into tbl_member values(?,?,?,?,?)");
            pstmt.setString(1, dto.getEmail());
            pstmt.setString(2, dto.getPwd());
            pstmt.setString(3, dto.getAddr1());
            pstmt.setString(4, dto.getAddr2());
            pstmt.setInt(5, dto.getGrade());

            int result = pstmt.executeUpdate();

            if(result > 0)
            {
                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                pstmt.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
}