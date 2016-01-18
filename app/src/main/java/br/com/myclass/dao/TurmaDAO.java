package br.com.myclass.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.myclass.helper.DataBaseHelper;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 31/07/15.
 */
public class TurmaDAO {
    private DataBaseHelper mHelper;
    private SQLiteDatabase db;

    public TurmaDAO(Context context) {
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

    public List<Turma> listar() {
        Cursor cursor = getDb().query(DataBaseHelper.Turma.TABELA,
                DataBaseHelper.Turma.COLUNAS, null, null, null, null, null);
        List<Turma> turmas = new ArrayList<Turma>();
        while (cursor.moveToNext()) {
            Turma turma = criarTurma(cursor);
            turmas.add(turma);
        }
        cursor.close();
        return turmas;
    }

    public List<Turma> listarAtivas() {
        String selection = DataBaseHelper.Turma.STATUS  + " = 'ativo'";
        Cursor cursor = getDb().query(DataBaseHelper.Turma.TABELA,
                DataBaseHelper.Turma.COLUNAS, selection, null, null, null, null);
        List<Turma> turmas = new ArrayList<Turma>();
        while (cursor.moveToNext()) {
            Turma turma = criarTurma(cursor);
            turmas.add(turma);
        }
        cursor.close();
        return turmas;
    }

    public Turma buscarPorId(Long id) {
        Cursor cursor = getDb().query(DataBaseHelper.Turma.TABELA,
                DataBaseHelper.Turma.COLUNAS,
                DataBaseHelper.Turma._ID + " = ?",
                new String[]{id.toString()}, null, null, null);
        if (cursor.moveToNext()) {
            Turma turma = criarTurma(cursor);
            cursor.close();
            return turma;
        }

        return null;
    }

    public long inserir(Turma turma) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Turma.DISCIPLINA, turma.getDisciplina());
        values.put(DataBaseHelper.Turma.CURSO, turma.getCurso());
        values.put(DataBaseHelper.Turma.DATAINICIAL, turma.getDataInicial());
        values.put(DataBaseHelper.Turma.DATAFINAL, turma.getDataFinal());
        values.put(DataBaseHelper.Turma.DESCRICAO, turma.getDescricao());
        values.put(DataBaseHelper.Turma.STATUS, turma.getStatus());

        return getDb().insert(DataBaseHelper.Turma.TABELA, null, values);
    }

    public long atualizar(Turma turma) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Turma.DISCIPLINA, turma.getDisciplina());
        values.put(DataBaseHelper.Turma.CURSO, turma.getCurso());
        values.put(DataBaseHelper.Turma.DATAINICIAL, turma.getDataInicial());
        values.put(DataBaseHelper.Turma.DATAFINAL, turma.getDataFinal());
        values.put(DataBaseHelper.Turma.DESCRICAO, turma.getDescricao());
        values.put(DataBaseHelper.Turma.STATUS, turma.getStatus());

        return getDb().update(DataBaseHelper.Turma.TABELA, values,
                DataBaseHelper.Turma._ID + " = ?",
                new String[]{turma.getId().toString()});
    }

    public boolean remover(Long id) {
        String whereClause = DataBaseHelper.Turma._ID + " = ?";
        String[] whereArgs = new String[]{id.toString()};
        int removidos = getDb().delete(DataBaseHelper.Turma.TABELA,
                whereClause, whereArgs);
        return removidos > 0;
    }


    private Turma criarTurma(Cursor cursor) {
        Turma turma = new Turma(
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Turma._ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Turma.DISCIPLINA)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Turma.CURSO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Turma.DATAINICIAL)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Turma.DATAFINAL)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Turma.DESCRICAO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Turma.STATUS)));
        return turma;
    }

}