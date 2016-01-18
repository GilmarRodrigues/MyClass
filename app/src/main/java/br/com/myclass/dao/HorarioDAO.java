package br.com.myclass.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.myclass.helper.DataBaseHelper;
import br.com.myclass.model.Horario;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 04/01/16.
 */
public class HorarioDAO {
    private DataBaseHelper mHelper;
    private SQLiteDatabase db;

    public HorarioDAO(Context context) {
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

    public List<Horario> listar(Turma turma, String status) {
        String selection = DataBaseHelper.Horario.TURMA_ID + " = ?";
        String[] selectionArgs = new String[]{turma.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.Horario.TABELA,
                DataBaseHelper.Horario.COLUNAS, selection, selectionArgs, null, null, null);
        List<Horario> horarios = new ArrayList<Horario>();
        while (cursor.moveToNext()) {
            Horario horario = criarHorario(cursor);
            if (horario.getStatus().equals(status)) {
                horarios.add(horario);
            }
        }
        cursor.close();
        return horarios;
    }

    public List<Horario> listar(String status) {
        String selection = DataBaseHelper.Horario.STATUS + " = ?";
        String[] selectionArgs = new String[]{status};

        Cursor cursor = getDb().query(DataBaseHelper.Horario.TABELA,
                DataBaseHelper.Horario.COLUNAS, selection, selectionArgs, null, null, null);
        List<Horario> horarios = new ArrayList<Horario>();
        while (cursor.moveToNext()) {
            Horario horario = criarHorario(cursor);
            horarios.add(horario);
        }
        cursor.close();
        return horarios;
    }

    public Horario buscarPorId(Long id) {
        Cursor cursor = getDb().query(DataBaseHelper.Horario.TABELA,
                DataBaseHelper.Horario.COLUNAS,
                DataBaseHelper.Horario._ID + "=?",
                new String[]{id.toString()}, null, null, null);
        if (cursor.moveToNext()) {
            Horario horario = criarHorario(cursor);
            cursor.close();
            return horario;
        }
        return null;
    }

    public Horario buscarPorId(Long id, Turma turma) {
        Cursor cursor = getDb().query(DataBaseHelper.Horario.TABELA,
                DataBaseHelper.Horario.COLUNAS,
                DataBaseHelper.Horario._ID + "=?",
                new String[]{id.toString()}, null, null, null);
        if (cursor.moveToNext()) {
            Horario horario = criarHorario(cursor);
            if (horario.getTurmaId().equals(turma.getId())) {
                cursor.close();
                return horario;
            }
        }
        return null;
    }

    public long inserir(Horario horario) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Horario.DIA, horario.getDia());
        values.put(DataBaseHelper.Horario.HORAINICIO, horario.getHoraInicio());
        values.put(DataBaseHelper.Horario.HORAFIM, horario.getHoraFim());
        values.put(DataBaseHelper.Horario.DESCRICAO, horario.getDescricao());
        values.put(DataBaseHelper.Horario.STATUS, horario.getStatus());
        values.put(DataBaseHelper.Horario.TURMA_ID, horario.getTurmaId());
        return getDb().insert(DataBaseHelper.Horario.TABELA, null, values);
    }

    public long atualizar(Horario horario) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Horario.DIA, horario.getDia());
        values.put(DataBaseHelper.Horario.HORAINICIO, horario.getHoraInicio());
        values.put(DataBaseHelper.Horario.HORAFIM, horario.getHoraFim());
        values.put(DataBaseHelper.Horario.DESCRICAO, horario.getDescricao());
        values.put(DataBaseHelper.Horario.STATUS, horario.getStatus());
        values.put(DataBaseHelper.Horario.TURMA_ID, horario.getTurmaId());
        return getDb().update(DataBaseHelper.Horario.TABELA, values, DataBaseHelper.Horario._ID + "=?", new String[]{horario.getId().toString()});
    }

    public boolean remover(Long id) {
        String whereClause = DataBaseHelper.Horario._ID + "=?";
        String[] whereArgs = new String[]{id.toString()};
        int removidos = getDb().delete(DataBaseHelper.Horario.TABELA,
                whereClause, whereArgs);
        return removidos > 0;
    }

    private Horario criarHorario(Cursor cursor) {
        Horario horario = new Horario(
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Horario._ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Horario.DIA)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Horario.HORAINICIO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Horario.HORAFIM)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Horario.DESCRICAO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Horario.STATUS)),
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Horario.TURMA_ID))
        );
        return horario;
    }
}
