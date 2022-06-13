package com.cqu.accountbook.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DatabaseLists {
    @Query("SELECT * FROM MyDatabase ORDER BY year DESC,month DESC,day DESC,id DESC")
    List<MyDatabase> getAllAccounts();

    @Query("SELECT * FROM MyDatabase WHERE not inOut ORDER BY year DESC,month DESC,day DESC,id DESC")
    List<MyDatabase> getAllIncomes();

    @Query("SELECT * FROM MyDatabase where inOut ORDER BY year DESC,month DESC,day DESC,id DESC")
    List<MyDatabase> getAllExpense();

    @Query("SELECT * FROM MyDatabase where (:io=0 or (inOut and :io=1) or (not inOut and :io=2)) and (:type='所有类别' or :type=type) and (money>=:fn and money<=:tn) and ((year=:yy and month=:mm) or :allDt)  ORDER BY year DESC,month DESC,day DESC,id DESC")
    List<MyDatabase> search(int io, String type, long fn, long tn, int yy, int mm, boolean allDt);

    @Query("SELECT * FROM MyDatabase WHERE (year=:y and month=:m and day=:d and inOut)")
    List<MyDatabase> getDayExpense(int y, int m, int d);

    @Query("SELECT sum(money) FROM MyDatabase where (year=:y and month=:m and day=:d and not inOut)")
    long dayIn(int y, int m, int d);

    @Query("SELECT sum(money) FROM MyDatabase where (year=:y and month=:m and day=:d and inOut)")
    long dayOut(int y, int m, int d);

    @Query("SELECT sum(money) FROM MyDatabase where (year=:y and month=:m and not inOut)")
    long getMonthI(int y, int m);

    @Query("SELECT sum(money) FROM MyDatabase where (year=:y and month=:m and inOut)")
    long getMonthO(int y, int m);

    @Query("SELECT sum(money) FROM MyDatabase where (year=:y and month=:m and day=:d and not inOut)")
    long getDayI(int y, int m, int d);

    @Query("SELECT sum(money) FROM MyDatabase where (year=:y and month=:m and day=:d and inOut)")
    long getDayO(int y, int m, int d);

    @Insert
    void insert(MyDatabase... incomes);

    @Update
    void update(MyDatabase... incomes);

    @Delete
    void delete(MyDatabase... incomes);

}