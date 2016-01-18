package br.com.myclass.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.myclass.helper.DataBaseHelper;
import br.com.myclass.model.Prova;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 28/12/15.
 */
public class ProvaDAO {
    private DataBaseHelper mHelper;
    private SQLiteDatabase db;

    public ProvaDAO(Context context) {
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

    public List<Prova> listar(Turma turma, String status) {
        String selection = DataBaseHelper.Prova.TURMA_ID + " = ?";
        String[] selectionArgs = new String[]{turma.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.Prova.TABELA,
                DataBaseHelper.Prova.COLUNAS, selection, selectionArgs, null, null, null);
        List<Prova> provas = new ArrayList<Prova>();
        while (cursor.moveToNext()) {
            Prova prova = criarProva(cursor);
            if (prova.getStatus().equals(status)) {
                provas.add(prova);
            }
        }
        cursor.close();
        return provas;
    }

    public Prova buscarPorId(Long id) {
        Cursor cursor = getDb().query(DataBaseHelper.Prova.TABELA,
                DataBaseHelper.Prova.COLUNAS,
                DataBaseHelper.Prova._ID + "=?",
                new String[]{id.toString()}, null, null, null);
        if (cursor.moveToNext()) {
            Prova prova = criarProva(cursor);
            cursor.close();
            return prova;
        }
        return null;
    }

    public long inserir(Prova prova) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Prova.DATA, prova.getData());
        values.put(DataBaseHelper.Prova.HORA, prova.getHora());
        values.put(DataBaseHelper.Prova.DESCRICAO, prova.getDescricao());
        values.put(DataBaseHelper.Prova.STATUS, prova.getStatus());
        values.put(DataBaseHelper.Prova.TURMA_ID, prova.getTurmaId());
        return getDb().insert(DataBaseHelper.Prova.TABELA, null, values);
    }

    public long atualizar(Prova prova) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Prova.DATA, prova.getData());
        values.put(DataBaseHelper.Prova.HORA, prova.getHora());
        values.put(DataBaseHelper.Prova.DESCRICAO, prova.getDescricao());
        values.put(DataBaseHelper.Prova.STATUS, prova.getStatus());
        values.put(DataBaseHelper.Prova.TURMA_ID, prova.getTurmaId());
        return getDb().update(DataBaseHelper.Prova.TABELA, values, DataBaseHelper.Prova._ID + "=?", new String[]{prova.getId().toString()});
    }

    public boolean remover(Long id) {
        String whereClause = DataBaseHelper.Prova._ID + "=?";
        String[] whereArgs = new String[]{id.toString()};
        int removidos = getDb().delete(DataBaseHelper.Prova.TABELA,
                whereClause, whereArgs);
        return removidos > 0;
    }

    private Prova criarProva(Cursor cursor) {
        Prova prova = new Prova(
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Prova._ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Prova.DATA)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Prova.HORA)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Prova.DESCRICAO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Prova.STATUS)),
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Prova.TURMA_ID))
        );
        return prova;
    }
}
