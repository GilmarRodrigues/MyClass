package br.com.myclass.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.myclass.helper.DataBaseHelper;
import br.com.myclass.model.Atividade;
import br.com.myclass.model.Aula;

/**
 * Created by gilmar on 23/12/15.
 */
public class AtividadeDAO {
    private DataBaseHelper mHelper;
    private SQLiteDatabase db;

    public AtividadeDAO(Context context) {
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

    public List<Atividade> listar(Aula aula, String status) {
        String selection = DataBaseHelper.Atividade.AULA_ID + " = ?";
        String[] selectionArgs = new String[]{aula.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.Atividade.TABELA,
                DataBaseHelper.Atividade.COLUNAS, selection, selectionArgs, null, null, null);
        List<Atividade> atividades = new ArrayList<Atividade>();
        while (cursor.moveToNext()) {
            Atividade atividade = criarAtividade(cursor);
            if (atividade.getStatus().equals(status)) {
                atividades.add(atividade);
            }
        }
        cursor.close();
        return atividades;
    }

    public List<Atividade> listAll() {
        Cursor cursor = getDb().query(DataBaseHelper.Atividade.TABELA,
                DataBaseHelper.Atividade.COLUNAS, null, null, null, null, null);
        List<Atividade> atividades = new ArrayList<Atividade>();
        while (cursor.moveToNext()) {
            Atividade atividade = criarAtividade(cursor);
            if (atividade.getStatus().equals("ativo")) {
                atividades.add(atividade);
            }
        }
        cursor.close();
        return atividades;
    }

    public List<Atividade> listFilter(Aula aula, String status) {
        String selection = DataBaseHelper.Atividade.AULA_ID + " = ?";
        String[] selectionArgs = new String[]{aula.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.Atividade.TABELA,
                DataBaseHelper.Atividade.COLUNAS, selection, selectionArgs, null, null, null);
        List<Atividade> atividades = new ArrayList<Atividade>();
        while (cursor.moveToNext()) {
            Atividade atividade = criarAtividade(cursor);
            if (atividade.getStatus().equals("ativo")) {
                atividades.add(atividade);
            }
        }
        cursor.close();
        return atividades;
    }

    public Atividade buscarPorId(Long id) {
        Cursor cursor = getDb().query(DataBaseHelper.Atividade.TABELA,
                DataBaseHelper.Atividade.COLUNAS,
                DataBaseHelper.Atividade._ID + "=?",
                new String[]{id.toString()}, null, null, null);
        if (cursor.moveToNext()) {
            Atividade atividade = criarAtividade(cursor);
            cursor.close();
            return atividade;
        }
        return null;
    }

    public Atividade buscarPorId(Long id, Aula aula) {
        Cursor cursor = getDb().query(DataBaseHelper.Atividade.TABELA,
                DataBaseHelper.Atividade.COLUNAS,
                DataBaseHelper.Atividade._ID + "=?",
                new String[]{id.toString()}, null, null, null);
        if (cursor.moveToNext()) {
            Atividade atividade = criarAtividade(cursor);
            if (atividade.getAulaId().equals(aula.getId())) {
                cursor.close();
                return atividade;
            }
        }
        return null;
    }

    public long inserir(Atividade atividade) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Atividade.TIPO, atividade.getTipo());
        values.put(DataBaseHelper.Atividade.DESCRICAO, atividade.getDescricao());
        values.put(DataBaseHelper.Atividade.STATUS, atividade.getStatus());
        values.put(DataBaseHelper.Atividade.AULA_ID, atividade.getAulaId());
        return getDb().insert(DataBaseHelper.Atividade.TABELA, null, values);
    }

    public long atualizar(Atividade atividade) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Atividade.TIPO, atividade.getTipo());
        values.put(DataBaseHelper.Atividade.DESCRICAO, atividade.getDescricao());
        values.put(DataBaseHelper.Atividade.STATUS, atividade.getStatus());
        values.put(DataBaseHelper.Atividade.AULA_ID, atividade.getAulaId());
        return getDb().update(DataBaseHelper.Atividade.TABELA, values, DataBaseHelper.Atividade._ID + "=?", new String[]{atividade.getId().toString()});
    }

    public boolean remover(Long id) {
        String whereClause = DataBaseHelper.Atividade._ID + "=?";
        String[] whereArgs = new String[]{id.toString()};
        int removidos = getDb().delete(DataBaseHelper.Atividade.TABELA,
                whereClause, whereArgs);
        return removidos > 0;
    }

    private Atividade criarAtividade(Cursor cursor) {
        Atividade atividade = new Atividade(
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Atividade._ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Atividade.TIPO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Atividade.DESCRICAO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Atividade.STATUS)),
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Atividade.AULA_ID))
                );
        return atividade;
    }
}
