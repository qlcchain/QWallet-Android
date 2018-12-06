package com.stratagile.qlink.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EOS_ACCOUNT".
*/
public class EosAccountDao extends AbstractDao<EosAccount, Long> {

    public static final String TABLENAME = "EOS_ACCOUNT";

    /**
     * Properties of entity EosAccount.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property AccountName = new Property(1, String.class, "accountName", false, "ACCOUNT_NAME");
        public final static Property ActivePrivateKey = new Property(2, String.class, "activePrivateKey", false, "ACTIVE_PRIVATE_KEY");
        public final static Property ActivePublicKey = new Property(3, String.class, "activePublicKey", false, "ACTIVE_PUBLIC_KEY");
        public final static Property OwnerPrivateKey = new Property(4, String.class, "ownerPrivateKey", false, "OWNER_PRIVATE_KEY");
        public final static Property OwnerPublicKey = new Property(5, String.class, "ownerPublicKey", false, "OWNER_PUBLIC_KEY");
        public final static Property IsCurrent = new Property(6, boolean.class, "isCurrent", false, "IS_CURRENT");
        public final static Property AccountPassword = new Property(7, String.class, "accountPassword", false, "ACCOUNT_PASSWORD");
    }


    public EosAccountDao(DaoConfig config) {
        super(config);
    }
    
    public EosAccountDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EOS_ACCOUNT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ACCOUNT_NAME\" TEXT," + // 1: accountName
                "\"ACTIVE_PRIVATE_KEY\" TEXT," + // 2: activePrivateKey
                "\"ACTIVE_PUBLIC_KEY\" TEXT," + // 3: activePublicKey
                "\"OWNER_PRIVATE_KEY\" TEXT," + // 4: ownerPrivateKey
                "\"OWNER_PUBLIC_KEY\" TEXT," + // 5: ownerPublicKey
                "\"IS_CURRENT\" INTEGER NOT NULL ," + // 6: isCurrent
                "\"ACCOUNT_PASSWORD\" TEXT);"); // 7: accountPassword
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EOS_ACCOUNT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, EosAccount entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String accountName = entity.getAccountName();
        if (accountName != null) {
            stmt.bindString(2, accountName);
        }
 
        String activePrivateKey = entity.getActivePrivateKey();
        if (activePrivateKey != null) {
            stmt.bindString(3, activePrivateKey);
        }
 
        String activePublicKey = entity.getActivePublicKey();
        if (activePublicKey != null) {
            stmt.bindString(4, activePublicKey);
        }
 
        String ownerPrivateKey = entity.getOwnerPrivateKey();
        if (ownerPrivateKey != null) {
            stmt.bindString(5, ownerPrivateKey);
        }
 
        String ownerPublicKey = entity.getOwnerPublicKey();
        if (ownerPublicKey != null) {
            stmt.bindString(6, ownerPublicKey);
        }
        stmt.bindLong(7, entity.getIsCurrent() ? 1L: 0L);
 
        String accountPassword = entity.getAccountPassword();
        if (accountPassword != null) {
            stmt.bindString(8, accountPassword);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, EosAccount entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String accountName = entity.getAccountName();
        if (accountName != null) {
            stmt.bindString(2, accountName);
        }
 
        String activePrivateKey = entity.getActivePrivateKey();
        if (activePrivateKey != null) {
            stmt.bindString(3, activePrivateKey);
        }
 
        String activePublicKey = entity.getActivePublicKey();
        if (activePublicKey != null) {
            stmt.bindString(4, activePublicKey);
        }
 
        String ownerPrivateKey = entity.getOwnerPrivateKey();
        if (ownerPrivateKey != null) {
            stmt.bindString(5, ownerPrivateKey);
        }
 
        String ownerPublicKey = entity.getOwnerPublicKey();
        if (ownerPublicKey != null) {
            stmt.bindString(6, ownerPublicKey);
        }
        stmt.bindLong(7, entity.getIsCurrent() ? 1L: 0L);
 
        String accountPassword = entity.getAccountPassword();
        if (accountPassword != null) {
            stmt.bindString(8, accountPassword);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public EosAccount readEntity(Cursor cursor, int offset) {
        EosAccount entity = new EosAccount( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // accountName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // activePrivateKey
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // activePublicKey
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // ownerPrivateKey
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // ownerPublicKey
            cursor.getShort(offset + 6) != 0, // isCurrent
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // accountPassword
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, EosAccount entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAccountName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setActivePrivateKey(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setActivePublicKey(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setOwnerPrivateKey(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setOwnerPublicKey(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIsCurrent(cursor.getShort(offset + 6) != 0);
        entity.setAccountPassword(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(EosAccount entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(EosAccount entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(EosAccount entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}