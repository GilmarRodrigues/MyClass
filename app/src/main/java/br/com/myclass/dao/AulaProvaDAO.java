package br.com.myclass.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.myclass.helper.DataBaseHelper;
import br.com.myclass.model.AulaProva;
import br.com.myclass.model.Prova;

/**
 * Created by gilmar on 28/12/15.
 */
public class AulaProvaDAO {
    private DataBaseHelper mHelper;
    private SQLiteDatabase db;

    public AulaProvaDAO(Context context) {
        mHelper = new DataBaseHelper(context);
    }

    private SQLiteDatabase getDb() {
        if (db == null) {
            db = mHelper.getWritableDatabase();
        }
        return db;
    }

    public void close() {
        mHelper.close();
        db = null;
    }

    public List<AulaProva> listar(Prova prova, String status) {
        String selection = DataBaseHelper.AulaProva.PROVA_ID + " = ?";
        String[] selectionArgs = new String[]{prova.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.AulaProva.TABELA,
                DataBaseHelper.AulaProva.COLUNAS, selection, selectionArgs, null, null, null);
        List<AulaProva> aulaProvas = new ArrayList<AulaProva>();
        while (cursor.moveToNext()) {
            AulaProva aulaProva = criarAulaProva(cursor);
            if (aulaProva.getStatus().equals(status)) {
                aulaProvas.add(aulaProva);
            }
        }
        cursor.close();
        return aulaProvas;
    }

    public AulaProva buscarPorId(Long id) {
        Cursor cursor = getDb().query(DataBaseHelper.AulaProva.TABELA,
                DataBaseHelper.AulaProva.COLUNAS,
                DataBaseHelper.AulaProva._ID + "=?",
                new String[]{id.toString()}, null, null, null);
        if (cursor.moveToNext()) {
            AulaProva aulaProva = criarAulaProva(cursor);
            cursor.close();
            return aulaProva;
        }
        return null;
    }

    public long inserir(AulaProva aulaProva) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.AulaProva.AULA_ID, aulaProva.getAulaId());
        values.put(DataBaseHelper.AulaProva.PROVA_ID, aulaProva.getProvaId());
        values.put(DataBaseHelper.AulaProva.STATUS, aulaProva.getStatus());
        return getDb().insert(DataBaseHelper.AulaProva.TABELA, null, values);
    }

    public long atualizar(AulaProva aulaProva) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.AulaProva.AULA_ID, aulaProva.getAulaId());
        values.put(DataBaseHelper.AulaProva.PROVA_ID, aulaProva.getProvaId());
        values.put(DataBaseHelper.AulaProva.STATUS, aulaProva.getStatus());
        return getDb().update(DataBaseHelper.AulaProva.TABELA, values, DataBaseHelper.AulaProva._ID + "=?", new String[]{aulaProva.getId().toString()});
    }

    public boolean remover(Long id) {
        String whereClause = DataBaseHelper.AulaProva._ID + "=?";
        String[] whereArgs = new String[]{id.toString()};
        int removidos = getDb().delete(DataBaseHelper.AulaProva.TABELA,
                whereClause, whereArgs);
        return removidos > 0;
    }
    
    private AulaProva criarAulaProva(Cursor cursor) {
        AulaProva aulaProva = new AulaProva(
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.AulaProva._ID)),
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.AulaProva.AULA_ID)),
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.AulaProva.PROVA_ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.AulaProva.STATUS))
                );
        return aulaProva;
    }
}
