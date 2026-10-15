{
  description = "Kassa App dev environment";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
    devshell = {
      url = "github:numtide/devshell";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs = { self, nixpkgs, flake-utils, devshell }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          overlays = [ devshell.overlays.default ];
        };
      in {
        devShells.default = pkgs.devshell.mkShell {
          name = "Kassa App";

          packages = [
            pkgs.jdk21
            pkgs.maven
            pkgs.postgresql_16
            pkgs.flyway            # optional CLI; Spring Boot also runs Flyway on boot
          ];

          env = [
            { name = "JAVA_HOME"; value = "${pkgs.jdk21}"; }
            { name = "PGDATA";    eval = "$PRJ_DATA_DIR/postgres"; }
            { name = "PGHOST";    eval = "$PGDATA"; }       # psql/createdb use the socket
            { name = "PGPORT";    value = "5433"; }         # keep off 5432 so a system Postgres won't clash
            # Datasource for Spring Boot. URL + username are safe to commit; password comes from .env.
            { name = "SPRING_DATASOURCE_URL";      eval = "jdbc:postgresql://localhost:$PGPORT/kassa_development"; }
            { name = "SPRING_DATASOURCE_USERNAME"; value = "kassa"; }
          ];

          serviceGroups.server.services = {
            app.command = "mvn spring-boot:run";
          };

          commands = [
            {
              name = "pg:setup";
              category = "database";
              help = "Init a project-local Postgres and create the kassa role";
              command = ''
                : "''${SPRING_DATASOURCE_PASSWORD:?set it in .env — run: cp .env.example .env}"
                initdb --encoding=UTF8 --no-locale --no-instructions -U postgres
                echo "listen_addresses = 'localhost'"      >> $PGDATA/postgresql.conf
                echo "port = $PGPORT"                       >> $PGDATA/postgresql.conf
                echo "unix_socket_directories = '$PGDATA'"  >> $PGDATA/postgresql.conf
                echo "CREATE USER kassa WITH PASSWORD '$SPRING_DATASOURCE_PASSWORD' CREATEDB;" \
                  | postgres --single -E postgres
              '';
            }
            {
              name = "pg:start";
              category = "database";
              help = "Start Postgres and ensure the dev + test databases exist";
              command = ''
                [ ! -d $PGDATA ] && pg:setup
                pg_ctl -D $PGDATA -U postgres start -l $PRJ_ROOT/postgres.log
                for db in kassa_development kassa_test; do
                  if ! psql -U postgres -lqt | cut -d '|' -f1 | grep -qw $db; then
                    createdb -U postgres -O kassa $db
                  fi
                done
              '';
            }
            {
              name = "pg:stop";
              category = "database";
              help = "Stop Postgres";
              command = "pg_ctl -D $PGDATA -U postgres stop";
            }
            {
              name = "pg:console";
              category = "database";
              help = "Open a psql console on the dev database";
              command = "psql -U postgres kassa_development";
            }
            {
              name = "pg:reset";
              category = "database";
              help = "Drop and recreate the dev database (wipes data)";
              command = ''
                dropdb -U postgres --if-exists kassa_development
                createdb -U postgres -O kassa kassa_development
              '';
            }
          ];
        };
      });
}