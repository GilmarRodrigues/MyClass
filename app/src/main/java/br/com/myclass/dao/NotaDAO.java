package br.com.myclass.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.myclass.helper.DataBaseHelper;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.Nota;
import br.com.myclass.model.Prova;

/**
 * Created by gilmar on 30/12/15.
 */
public class NotaDAO {
    private DataBaseHelper mHelper;
    private SQLiteDatabase db;

    public NotaDAO(Context context) {
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

    public List<Nota> listar(Prova prova, String status) {
        String selection = DataBaseHelper.Nota.PROVA_ID + " = ?";
        String[] selectionArgs = new String[]{prova.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.Nota.TABELA,
                DataBaseHelper.Nota.COLUNAS, selection, selectionArgs, null, null, null);
        List<Nota> notas = new ArrayList<Nota>();
        while (cursor.moveToNext()) {
            Nota nota = criarNota(cursor);
            if (nota.getStatus().equals(status)) {
                notas.add(nota);
            }
        }
        cursor.close();
        return notas;
    }

    public List<Nota> listar(Aluno aluno, String status) {
        String selection = DataBaseHelper.Nota.ALUNO_ID + " = ?";
        String[] selectionArgs = new String[]{aluno.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.Nota.TABELA,
                DataBaseHelper.Nota.COLUNAS, selection, selectionArgs, null, null, null);
        List<Nota> notas = new ArrayList<Nota>();
        while (cursor.moveToNext()) {
            Nota nota = criarNota(cursor);
            if (nota.getStatus().equals(status)) {
                notas.add(nota);
            }
        }
        cursor.close();
        return notas;
    }

    public Nota buscarPorId(Long id) {
        Cursor cursor = getDb().query(DataBaseHelper.Nota.TABELA,
                DataBaseHelper.Nota.COLUNAS,
                DataBaseHelper.Nota._ID + "=?",
                new String[]{id.toString()}, null, null, null);
        if (cursor.moveToNext()) {
            Nota nota = criarNota(cursor);
            cursor.close();
            return nota;
        }
        return null;
    }

    public long inserir(Nota nota) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Nota.NOTA, nota.getNota());
        values.put(DataBaseHelper.Nota.PONTO_EXTRA, nota.getPontoExtra());
        values.put(DataBaseHelper.Nota.DESCRICAO, nota.getDescricao());
        values.put(DataBaseHelper.Nota.STATUS, nota.getStatus());
        values.put(DataBaseHelper.Nota.ALUNO_ID, nota.getAlunoId());
        values.put(DataBaseHelper.Nota.PROVA_ID, nota.getProvaId());
        return getDb().insert(DataBaseHelper.Nota.TABELA, null, values);
    }

    public long atualizar(Nota nota) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Nota.NOTA, nota.getNota());
        values.put(DataBaseHelper.Nota.PONTO_EXTRA, nota.getPontoExtra());
        values.put(DataBaseHelper.Nota.DESCRICAO, nota.getDescricao());
        values.put(DataBaseHelper.Nota.STATUS, nota.getStatus());
        values.put(DataBaseHelper.Nota.ALUNO_ID, nota.getAlunoId());
        values.put(DataBaseHelper.Nota.PROVA_ID, nota.getProvaId());
        return getDb().update(DataBaseHelper.Nota.TABELA, values, DataBaseHelper.Nota._ID + "=?", new String[]{nota.getId().toString()});
    }

    public boolean remover(Long id) {
        String whereClause = DataBaseHelper.Nota._ID + "=?";
        String[] whereArgs = new String[]{id.toString()};
        int removidos = getDb().delete(DataBaseHelper.Nota.TABELA,
                whereClause, whereArgs);
        return removidos > 0;
    }

    private Nota criarNota(Cursor cursor) {
        Nota nota = new Nota(
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Nota._ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Nota.NOTA)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Nota.PONTO_EXTRA)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Nota.DESCRICAO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Nota.STATUS)),
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Nota.ALUNO_ID)),
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Nota.PROVA_ID))

        );
        return nota;
    }
}

