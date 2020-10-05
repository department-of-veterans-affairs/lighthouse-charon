FROM vasdvp/health-apis-maven:3.6-jdk-14

COPY maven/ /vistalink-tests
RUN chmod 755 /vistalink-tests/*.sh
ENTRYPOINT ["/vistalink-tests/entrypoint.sh"]
CMD []
