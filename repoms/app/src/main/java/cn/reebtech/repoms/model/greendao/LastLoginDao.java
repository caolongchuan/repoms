package cn.reebtech.repoms.model.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.reebtech.repoms.model.entity.LastLogin;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LAST_LOGIN".
*/
public class LastLoginDao extends AbstractDao<LastLogin, Long> {

    public static final String TABLENAME = "LAST_LOGIN";

    /**
     * Properties of entity LastLogin.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Password = new Property(2, String.class, "password", false, "PASSWORD");
        public final static Property Rememberme = new Property(3, boolean.class, "rememberme", false, "REMEMBERME");
        public final static Property Autologin = new Property(4, boolean.class, "autologin", false, "AUTOLOGIN");
        public final static Property Date = new Property(5, java.util.Date.class, "date", false, "DATE");
    }


    public LastLoginDao(DaoConfig config) {
        super(config);
    }
    
    public LastLoginDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LAST_LOGIN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"PASSWORD\" TEXT," + // 2: password
                "\"REMEMBERME\" INTEGER NOT NULL ," + // 3: rememberme
                "\"AUTOLOGIN\" INTEGER NOT NULL ," + // 4: autologin
                "\"DATE\" INTEGER);"); // 5: date
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_LAST_LOGIN__id_DESC ON \"LAST_LOGIN\"" +
                " (\"_id\" DESC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LAST_LOGIN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, LastLogin entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(3, password);
        }
        stmt.bindLong(4, entity.getRememberme() ? 1L: 0L);
        stmt.bindLong(5, entity.getAutologin() ? 1L: 0L);
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(6, date.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, LastLogin entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(3, password);
        }
        stmt.bindLong(4, entity.getRememberme() ? 1L: 0L);
        stmt.bindLong(5, entity.getAutologin() ? 1L: 0L);
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(6, date.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public LastLogin readEntity(Cursor cursor, int offset) {
        LastLogin entity = new LastLogin( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // password
            cursor.getShort(offset + 3) != 0, // rememberme
            cursor.getShort(offset + 4) != 0, // autologin
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)) // date
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, LastLogin entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setPassword(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRememberme(cursor.getShort(offset + 3) != 0);
        entity.setAutologin(cursor.getShort(offset + 4) != 0);
        entity.setDate(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(LastLogin entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(LastLogin entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(LastLogin entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
