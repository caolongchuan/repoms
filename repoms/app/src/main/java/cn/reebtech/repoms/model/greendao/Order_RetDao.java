package cn.reebtech.repoms.model.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.reebtech.repoms.model.entity.Order_Ret;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ORDER__RET".
*/
public class Order_RetDao extends AbstractDao<Order_Ret, Long> {

    public static final String TABLENAME = "ORDER__RET";

    /**
     * Properties of entity Order_Ret.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Pid = new Property(0, Long.class, "pid", true, "_id");
        public final static Property Id = new Property(1, String.class, "id", false, "ID");
        public final static Property Location = new Property(2, String.class, "location", false, "LOCATION");
        public final static Property Retdate = new Property(3, java.util.Date.class, "retdate", false, "RETDATE");
        public final static Property Userret = new Property(4, String.class, "userret", false, "USERRET");
        public final static Property Usermgr = new Property(5, String.class, "usermgr", false, "USERMGR");
        public final static Property Incount = new Property(6, int.class, "incount", false, "INCOUNT");
        public final static Property Inprice = new Property(7, double.class, "inprice", false, "INPRICE");
        public final static Property Completed = new Property(8, boolean.class, "completed", false, "COMPLETED");
        public final static Property Uploaded = new Property(9, boolean.class, "uploaded", false, "UPLOADED");
        public final static Property Remark = new Property(10, String.class, "remark", false, "REMARK");
    }


    public Order_RetDao(DaoConfig config) {
        super(config);
    }
    
    public Order_RetDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ORDER__RET\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: pid
                "\"ID\" TEXT NOT NULL ," + // 1: id
                "\"LOCATION\" TEXT NOT NULL ," + // 2: location
                "\"RETDATE\" INTEGER NOT NULL ," + // 3: retdate
                "\"USERRET\" TEXT NOT NULL ," + // 4: userret
                "\"USERMGR\" TEXT NOT NULL ," + // 5: usermgr
                "\"INCOUNT\" INTEGER NOT NULL ," + // 6: incount
                "\"INPRICE\" REAL NOT NULL ," + // 7: inprice
                "\"COMPLETED\" INTEGER NOT NULL ," + // 8: completed
                "\"UPLOADED\" INTEGER NOT NULL ," + // 9: uploaded
                "\"REMARK\" TEXT);"); // 10: remark
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_ORDER__RET_ID ON \"ORDER__RET\"" +
                " (\"ID\" ASC);");
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_ORDER__RET_ID_DESC ON \"ORDER__RET\"" +
                " (\"ID\" DESC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ORDER__RET\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Order_Ret entity) {
        stmt.clearBindings();
 
        Long pid = entity.getPid();
        if (pid != null) {
            stmt.bindLong(1, pid);
        }
        stmt.bindString(2, entity.getId());
        stmt.bindString(3, entity.getLocation());
        stmt.bindLong(4, entity.getRetdate().getTime());
        stmt.bindString(5, entity.getUserret());
        stmt.bindString(6, entity.getUsermgr());
        stmt.bindLong(7, entity.getIncount());
        stmt.bindDouble(8, entity.getInprice());
        stmt.bindLong(9, entity.getCompleted() ? 1L: 0L);
        stmt.bindLong(10, entity.getUploaded() ? 1L: 0L);
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(11, remark);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Order_Ret entity) {
        stmt.clearBindings();
 
        Long pid = entity.getPid();
        if (pid != null) {
            stmt.bindLong(1, pid);
        }
        stmt.bindString(2, entity.getId());
        stmt.bindString(3, entity.getLocation());
        stmt.bindLong(4, entity.getRetdate().getTime());
        stmt.bindString(5, entity.getUserret());
        stmt.bindString(6, entity.getUsermgr());
        stmt.bindLong(7, entity.getIncount());
        stmt.bindDouble(8, entity.getInprice());
        stmt.bindLong(9, entity.getCompleted() ? 1L: 0L);
        stmt.bindLong(10, entity.getUploaded() ? 1L: 0L);
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(11, remark);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Order_Ret readEntity(Cursor cursor, int offset) {
        Order_Ret entity = new Order_Ret( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // pid
            cursor.getString(offset + 1), // id
            cursor.getString(offset + 2), // location
            new java.util.Date(cursor.getLong(offset + 3)), // retdate
            cursor.getString(offset + 4), // userret
            cursor.getString(offset + 5), // usermgr
            cursor.getInt(offset + 6), // incount
            cursor.getDouble(offset + 7), // inprice
            cursor.getShort(offset + 8) != 0, // completed
            cursor.getShort(offset + 9) != 0, // uploaded
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // remark
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Order_Ret entity, int offset) {
        entity.setPid(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setId(cursor.getString(offset + 1));
        entity.setLocation(cursor.getString(offset + 2));
        entity.setRetdate(new java.util.Date(cursor.getLong(offset + 3)));
        entity.setUserret(cursor.getString(offset + 4));
        entity.setUsermgr(cursor.getString(offset + 5));
        entity.setIncount(cursor.getInt(offset + 6));
        entity.setInprice(cursor.getDouble(offset + 7));
        entity.setCompleted(cursor.getShort(offset + 8) != 0);
        entity.setUploaded(cursor.getShort(offset + 9) != 0);
        entity.setRemark(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Order_Ret entity, long rowId) {
        entity.setPid(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Order_Ret entity) {
        if(entity != null) {
            return entity.getPid();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Order_Ret entity) {
        return entity.getPid() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
