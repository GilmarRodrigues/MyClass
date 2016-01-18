package br.com.myclass.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.myclass.helper.DataBaseHelper;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.AlunoAtividade;
import br.com.myclass.model.Atividade;

/**
 * Created by gilmar on 27/12/15.
 */
public class AlunoAtividadeDAO {
    private DataBaseHelper mHelper;
    private SQLiteDatabase db;

    public AlunoAtividadeDAO(Context context) {
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

    public List<AlunoAtividade> listar(Atividade atividade) {
        String selection = DataBaseHelper.AlunoAtividade.ATIVIDADE_ID + " = ?";
        String[] selectionArgs = new String[]{atividade.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.AlunoAtividade.TABELA,
                DataBaseHelper.AlunoAtividade.COLUNAS, selection, selectionArgs, null, null, null);
        List<AlunoAtividade> alunoAtividades = new ArrayList<AlunoAtividade>();
        while (cursor.moveToNext()) {
            AlunoAtividade alunoAtividade = criarAlunoAtividade(cursor);
            alunoAtividades.add(alunoAtividade);
        }
        cursor.close();
        return alunoAtividades;
    }

    public List<AlunoAtividade> listar(Atividade atividade, String status) {
        String selection = DataBaseHelper.AlunoAtividade.ATIVIDADE_ID + " = ?";
        String[] selectionArgs = new String[]{atividade.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.AlunoAtividade.TABELA,
                DataBaseHelper.AlunoAtividade.COLUNAS, selection, selectionArgs, null, null, null);
        List<AlunoAtividade> alunoAtividades = new ArrayList<AlunoAtividade>();
        while (cursor.moveToNext()) {
            AlunoAtividade alunoAtividade = criarAlunoAtividade(cursor);
            if (alunoAtividade.getStatus().equals(status)) {
                alunoAtividades.add(alunoAtividade);
            }
        }
        cursor.close();
        return alunoAtividades;
    }

    public List<AlunoAtividade> listar(Aluno aluno) {
        String selection = DataBaseHelper.AlunoAtividade.ALUNO_ID + " = ?";
        String[] selectionArgs = new String[]{aluno.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.AlunoAtividade.TABELA,
                DataBaseHelper.AlunoAtividade.COLUNAS, selection, selectionArgs, null, null, null);
        List<AlunoAtividade> alunoAtividades = new ArrayList<AlunoAtividade>();
        while (cursor.moveToNext()) {
            AlunoAtividade alunoAtividade = criarAlunoAtividade(cursor);
            alunoAtividades.add(alunoAtividade);
        }
        cursor.close();
        return alunoAtividades;
    }

    public List<AlunoAtividade> listarAtividadesFeitas(Aluno aluno) {
        String selection = DataBaseHelper.AlunoAtividade.ALUNO_ID + " = ?";
        String[] selectionArgs = new String[]{aluno.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.AlunoAtividade.TABELA,
                DataBaseHelper.AlunoAtividade.COLUNAS, selection, selectionArgs, null, null, null);
        List<AlunoAtividade> alunoAtividades = new ArrayList<AlunoAtividade>();
        while (cursor.moveToNext()) {
            AlunoAtividade alunoAtividade = criarAlunoAtividade(cursor);
            if (alunoAtividade.getStatus().equals("feito")) {
                alunoAtividades.add(alunoAtividade);
            }
        }
        cursor.close();
        return alunoAtividades;
    }


    public AlunoAtividade buscaPorId(Long id) {
        Cursor cursor = getDb().query(DataBaseHelper.AlunoAtividade.TABELA,
                DataBaseHelper.AlunoAtividade.COLUNAS,
                DataBaseHelper.AlunoAtividade._ID + "=?",
                new String[]{id.toString()}, null, null, null);
        if (cursor.moveToNext()) {
            AlunoAtividade alunoAtividade = criarAlunoAtividade(cursor);
            cursor.close();
            return alunoAtividade;
        }
        return null;
    }

    public long inserir(AlunoAtividade alunoAtividade) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.AlunoAtividade.ALUNO_ID, alunoAtividade.getAlunoId());
        values.put(DataBaseHelper.AlunoAtividade.ATIVIDADE_ID, alunoAtividade.getAtividadeId());
        values.put(DataBaseHelper.AlunoAtividade.STATUS, alunoAtividade.getStatus());
        return getDb().insert(DataBaseHelper.AlunoAtividade.TABELA, null, values);
    }

    public long atualizar(AlunoAtividade alunoAtividade) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.AlunoAtividade.ALUNO_ID, alunoAtividade.getAlunoId());
        values.put(DataBaseHelper.AlunoAtividade.ATIVIDADE_ID, alunoAtividade.getAtividadeId());
        values.put(DataBaseHelper.AlunoAtividade.STATUS, alunoAtividade.getStatus());
        return getDb().update(DataBaseHelper.AlunoAtividade.TABELA, values, DataBaseHelper.AlunoAtividade._ID + "=?", new String[]{alunoAtividade.getId().toString()});
    }


    public boolean remover(Long id) {
        String whereClause = DataBaseHelper.AlunoAtividade._ID + "=?";
        String[] whereArgs = new String[]{id.toString()};
        int removidos = getDb().delete(DataBaseHelper.AlunoAtividade.TABELA,
                whereClause, whereArgs);
        return removidos > 0;
    }

    private AlunoAtividade criarAlunoAtividade(Cursor cursor) {
        AlunoAtividade alunoAtividade = new AlunoAtividade(
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.AlunoAtividade._ID)),
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.AlunoAtividade.ALUNO_ID)),
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.AlunoAtividade.ATIVIDADE_ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.AlunoAtividade.STATUS))
        );
        return alunoAtividade;
    }

}
