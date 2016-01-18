package br.com.myclass.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.myclass.helper.DataBaseHelper;
import br.com.myclass.model.Aula;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 21/12/15.
 */
public class AulaDAO {
    private DataBaseHelper mHelper;
    private SQLiteDatabase db;

    public AulaDAO(Context context) {
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

    public List<Aula> listar(Turma turma) {
        String selection = DataBaseHelper.Aula.TURMA_ID + " = ?";
        String[] selectionArgs = new String[]{turma.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.Aula.TABELA,
                DataBaseHelper.Aula.COLUNAS, selection, selectionArgs, null, null, null);
        List<Aula> aulas = new ArrayList<Aula>();
        while (cursor.moveToNext()) {
            Aula aula = criarAula(cursor);
            if (aula.getStatus().equals("ativo")) {
                aulas.add(aula);
            }
        }
        cursor.close();
        return aulas;
    }

    public Aula buscarPorId(Long id) {
        Cursor cursor = getDb().query(DataBaseHelper.Aula.TABELA,
                DataBaseHelper.Aula.COLUNAS,
                DataBaseHelper.Aula._ID + "=?",
                new String[]{id.toString()}, null, null, null);
        if (cursor.moveToNext()) {
            Aula aula = criarAula(cursor);
            cursor.close();
            return aula;
        }
        return null;
    }

    public long inserir(Aula aula) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Aula.CONTEUDO, aula.getConteudo());
        values.put(DataBaseHelper.Aula.DATA, aula.getData());
        values.put(DataBaseHelper.Aula.HORAINICIO, aula.getHoraInicio());
        values.put(DataBaseHelper.Aula.HORAFIM, aula.getHoraFim());
        values.put(DataBaseHelper.Aula.DESCRICAO, aula.getDescricao());
        values.put(DataBaseHelper.Aula.STATUS, aula.getStatus());
        values.put(DataBaseHelper.Aula.TURMA_ID, aula.getTurmaId());
        return getDb().insert(DataBaseHelper.Aula.TABELA, null, values);
    }

    public long atualizar(Aula aula) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Aula.CONTEUDO, aula.getConteudo());
        values.put(DataBaseHelper.Aula.DATA, aula.getData());
        values.put(DataBaseHelper.Aula.HORAINICIO, aula.getHoraInicio());
        values.put(DataBaseHelper.Aula.HORAFIM, aula.getHoraFim());
        values.put(DataBaseHelper.Aula.DESCRICAO, aula.getDescricao());
        values.put(DataBaseHelper.Aula.STATUS, aula.getStatus());
        values.put(DataBaseHelper.Aula.TURMA_ID, aula.getTurmaId());
        return getDb().update(DataBaseHelper.Aula.TABELA, values, DataBaseHelper.Aula._ID + "=?", new String[]{aula.getId().toString()});
    }

    public boolean remover(Long id) {
        String whereClause = DataBaseHelper.Aula._ID + "=?";
        String[] whereArgs = new String[]{id.toString()};
        int removidos = getDb().delete(DataBaseHelper.Aula.TABELA,
                whereClause, whereArgs);
        return removidos > 0;
    }


    private Aula criarAula(Cursor cursor) {
        Aula aula = new Aula(
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Aula._ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aula.CONTEUDO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aula.DATA)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aula.HORAINICIO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aula.HORAFIM)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aula.DESCRICAO)),
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Aula.TURMA_ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aula.STATUS))
        );
        return aula;
    }
}
