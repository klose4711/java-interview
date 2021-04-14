package com.recruiting.interview;

import com.recruiting.interview.outofscope.OutOfScopeSqlMock;

public class SqlRequest {
    final private String connstrg;
    final private String type;

    public SqlRequest(final String connstrg, final String type) {
        this.connstrg = connstrg;
        this.type = type;
        System.out.println("LOG: Created a connection to DB "
            + this.connstrg + "!");
    }

    public <T> T execute(String sql, Class<T> returntype)
            throws RuntimeException {

        final Application app = new Application();

        if (type.equals(app.TYPE_ONE)) {
            sql = sql + "WHERE role = Manager";
            System.out.println("LOG: SELECTING " + sql);
        } else if (type.equals(app.TYPE_TWO)) {
            sql = sql + " AND role = Tester";
            System.out.println("LOG: SELECTING " + sql);
        } else if (type.equals(app.TYPE_TRE)) {
            sql = sql + " AND role = Dev";
            System.out.println("LOG: SELECTING " + sql);
        } else if (type.equals(app.TYPE_FUR)) {
            sql = sql + " AND role = Student";
            System.out.println("LOG: SELECTING " + sql);
        }

        ///////////////////////////////////////////////////////////////////////////////////////////
        //
        // The scope of this exercise ends here!
        //
        //
        //
        // Assume that this actually does IO now by reaching out to an SQL DB over
        // the network and parses the response
        //
        ///////////////////////////////////////////////////////////////////////////////////////////

        return OutOfScopeSqlMock.execute(type, sql, returntype);
    }
}
