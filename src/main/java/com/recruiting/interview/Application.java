package com.recruiting.interview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Takes as a first argument a name of an executive and prints out which users 
 * work under him and what their departments, salaries and roles are.
 *
 * It collects data from several tables, correlates different user types by 
 * name and fills in the blanks if a user has missing information on a table.
 * Students don't get a salary even if they have other roles with values in 
 * their salary field. That is company policy and not negotiable.
 * 
 * Note:
 * This application is supposed to be a reporting type application. This means
 * that it doesn't concern itself with putting data into the database. It just 
 * reads it out and corrects inconsistencies (if needed) after the reads.
 *
 * Assignment:
 * 1. Read the codebase and understand.
 * 2. Things to think about:
 *    - Is this good code?
 *    - Is the output of the application suited for an executive tool?
 *    - How would you test this code?
 *    - Is the code intuitive?
 *    - Are there things that should be refactored to make the code more 
 *      understandable?
 *    - Are there things that should be refactored to make it easier to write
 *      unit tests focusing on only one part of the logic?
 *    - Are there error cases that must be dealt with?
 */
public class Application {

    public static class User {
        public String name;
        public String birth;
        public Integer salary;
        public String department;
        public String role;

        public User() {
        }

        public User(String name, String birth, Integer salary,
                String department) {
            super();
            this.name = name;
            this.birth = birth;
            this.salary = salary;
            this.department = department;
        };

        public String toString() {
            return name + " " + birth + " " + department + " " + salary + " "
                + role;
        }
    }
    public static class Users extends ArrayList<User> {
    };

    static class Where {
        public String where;
    }

    public static void main(final String[] args) {
        new Application().runLogic(args[0]);
    }

    public final String TYPE_ONE = "1";
    public final String TYPE_TWO = "2";
    public final String TYPE_TRE = "3";
    public final String TYPE_FUR = "4";

    public void runLogic(final String executive) {
        String sql = "Select name, birth, dept FROM users WHERE  supervisor='"
            + executive + "';";
        Users mgrs = new SqlRequest("sql://users.db/users", TYPE_ONE)
                .execute(sql, Users.class);

        for (User mgr : mgrs) {

            Where where = new Where();
            addWhereFilter(where, mgr);
            Users testers = new SqlRequest("sql://users.db/users", TYPE_TWO)
                    .execute(
                            "Select name, birth, dept FROM users" + where.where,
                            Users.class);
            Users devs = new SqlRequest("sql://users.db/users", TYPE_TRE)
                    .execute(
                            "Select name, birth, dept FROM users" + where.where,
                            Users.class);
            Users students = new SqlRequest("sql://users.db/users", TYPE_FUR)
                    .execute(
                            "Select name, birth, dept FROM users" + where.where,
                            Users.class);

            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println(
                    "Manager " + mgr.name + " " + mgr.salary + " ("
                        + mgr.department + ")  manages:");

            Map<String, User> persons = new HashMap<String, User>();
            for (User p : testers) {
                p.role = "Tester";
                persons.put(p.name, p);
            }
            for (User p : devs) {
                p.role = "Developer";
                User existing = persons.get(p.name);
                if (existing == null) {
                    persons.put(p.name, p);
                } else {
                    mergeUser(p, existing);
                }
            }
            for (User p : students) {
                p.role = "Student";
                User existing = persons.get(p.name);
                if (existing == null) {
                    persons.put(p.name, p);
                } else {
                    mergeUser(p, existing);
                }
                existing = persons.get(p.name);
                existing.salary = 0;
            }
            System.out.println("");
            System.out.println("");

            for (User p : persons.values()) {
                if (p.department.matches(".*" + mgr.department + ".*"))
                    System.out.println(p);
            }

            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("");
        }
    }

    private void addWhereFilter(Where where, User user) {
        where.where = " WHERE supervisor='";
        where.where = where.where + user.name;
        where.where = where.where + "'";
    }

    // Merges information from User one into user two
    private void mergeUser(final User one, final User two) {
        System.out.println("LOG: Merging: " + one + " into: " + two + " ");

        two.name = one.name == null ? two.name : one.name;
        two.birth = one.birth == null ? two.birth : one.birth;
        two.salary = two.salary + one.salary;
        two.role = two.role + " " + one.role;
        if (!two.department.equals(one.department)) {
            two.department = two.department + " " + one.department;
        }
    }

}
