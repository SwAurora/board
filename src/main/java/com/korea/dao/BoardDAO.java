package com.korea.dao;

import com.beust.ah.A;
import com.korea.dto.BoardDTO;
import com.korea.dto.ReplyDTO;
import lombok.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDAO
{
    private String url = "jdbc:mysql://localhost:3306/board";
    private String id = "root";
    private String pw = "1234";

    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    private static BoardDAO instance;

    public static BoardDAO getInstance()
    {
        if(instance == null)
        {
            instance = new BoardDAO();
        }
        return instance;
    }

    private BoardDAO()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, id, pw);
            System.out.println("DB Connected.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // 시작페이지, 끝페이지 번호 받아서 조회
    public List<BoardDTO> Select(int start, int end)
    {
        ArrayList<BoardDTO> list = new ArrayList<>();
        BoardDTO dto = null;
        try
        {
            pstmt = conn.prepareStatement("select * from tbl_board order by no desc limit ?,?");
            pstmt.setInt(1, start);
            pstmt.setInt(2, end);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                dto = new BoardDTO();
                dto.setNo(rs.getInt("no"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setWriter(rs.getString("writer"));
                dto.setRegdate(rs.getString("regdate"));
                dto.setPwd(rs.getString("pwd"));
                dto.setIp(rs.getString("ip"));
                dto.setFilename(rs.getString("filename"));
                dto.setFilesize(rs.getString("filesize"));
                dto.setCount(rs.getInt("count"));
                list.add(dto);
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
                rs.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                pstmt.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return list;
    }

    public int getTotalCount()
    {
        int result = 0;
        try
        {
            pstmt = conn.prepareStatement("select count(*) from tbl_board");
            rs = pstmt.executeQuery();
            rs.next();
            result = rs.getInt(1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                rs.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                pstmt.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean insert(BoardDTO dto)
    {
        try
        {
            pstmt = conn.prepareStatement("insert into tbl_board(title, content, writer, regdate, pwd, count, ip, filename, filesize) values(?,?,?,sysdate(),?,0,?,?,?)");
            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getContent());
            pstmt.setString(3, dto.getWriter());
            pstmt.setString(4, dto.getPwd());
            pstmt.setString(5, dto.getIp());
            pstmt.setString(6, dto.getFilename());
            pstmt.setString(7, dto.getFilesize());

            int result = pstmt.executeUpdate();
            if(result > 0)
                return true;
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

    public BoardDTO Select(int no)
    {
        BoardDTO dto = new BoardDTO();
        try
        {
            pstmt = conn.prepareStatement("select * from tbl_board where no = ?");
            pstmt.setInt(1, no);
            rs = pstmt.executeQuery();
            if(rs.next())
            {
                dto.setWriter(rs.getString("writer"));
                dto.setContent(rs.getString("content"));
                dto.setTitle(rs.getString("title"));
                dto.setPwd(rs.getString("pwd"));
                dto.setNo(rs.getInt("no"));
                dto.setIp(rs.getString("ip"));
                dto.setFilename(rs.getString("filename"));
                dto.setFilesize(rs.getString("filesize"));
                dto.setCount(rs.getInt("count"));
                dto.setRegdate(rs.getString("regdate"));
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
                rs.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                pstmt.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return dto;
    }

    public int getLastNo()
    {
        try
        {
            pstmt = conn.prepareStatement("ANALYZE TABLE tbl_board");
            pstmt.executeQuery();
            pstmt = conn.prepareStatement("SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = 'tbl_board' AND table_schema = 'board'");
            rs = pstmt.executeQuery();
            rs.next();
            System.out.println(rs.getInt(1));
            return rs.getInt(1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                rs.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                pstmt.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public void CountUp(int no)
    {
        try
        {
            pstmt = conn.prepareStatement("update tbl_board set count = count + 1 where no = ?");
            pstmt.setInt(1, no);
            pstmt.executeUpdate();
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
    }

    public boolean Update(BoardDTO dto)
    {
        try
        {
            pstmt = conn.prepareStatement("update tbl_board set title= ?, content = ? where no = ?");
            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getContent());
            pstmt.setInt(3, dto.getNo());
            int result = pstmt.executeUpdate();
            if(result > 0)
                return true;
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

    public boolean Delete(BoardDTO dto)
    {
        try
        {
            pstmt = conn.prepareStatement("delete from tbl_board where no = ?");
            pstmt.setInt(1, dto.getNo());
            int result = pstmt.executeUpdate();
            if(result > 0)
                return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean replypost(ReplyDTO rdto)
    {
        try
        {
            pstmt = conn.prepareStatement("insert into tbl_reply(bno, writer, content, regdate) values (?,?,?,sysdate())");
            pstmt.setInt(1, rdto.getBno());
            pstmt.setString(2, rdto.getWriter());
            pstmt.setString(3, rdto.getContent());
            int result = pstmt.executeUpdate();
            if(result > 0)
                return true;
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

    public ArrayList<ReplyDTO> getReplylist(int bno)
    {
        ArrayList<ReplyDTO> list = new ArrayList<>();
        ReplyDTO dto = null;
        try
        {
            pstmt = conn.prepareStatement("select * from tbl_reply where bno = ? order by rno desc");
            pstmt.setInt(1, bno);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                dto = new ReplyDTO();
                dto.setRno(rs.getInt("rno"));
                dto.setBno(rs.getInt("bno"));
                dto.setContent(rs.getString("content"));
                dto.setWriter(rs.getString("writer"));
                dto.setRegdate(rs.getString("regdate"));
                list.add(dto);
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
                rs.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                pstmt.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return list;
    }

    public int getTotalReplyCnt(int bno)
    {
        int tcnt = 0;
        try
        {
            pstmt = conn.prepareStatement("select count(*) from tbl_reply where bno = ?");
            pstmt.setInt(1, bno);
            rs = pstmt.executeQuery();
            rs.next();
            tcnt = rs.getInt(1);
            return tcnt;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                rs.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                pstmt.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
