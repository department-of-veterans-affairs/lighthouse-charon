# Working with RPCs

- [Searching for an RPC](#searching-for-an-rpc)
- [Adding context access to your user](#adding-context-access-to-your-user)


## Searching for an RPC

1. `S DUZ=1`
2. `D ^XUP`  
   ```
   Setting up programmer environment
   This is a TEST account.

   Terminal Type set to: C-VT320

   Select OPTION NAME:
   ```
3. `OPTION NAME` is `EVE`
   ```
     1   EVE       Systems Manager Menu
     2   EVET BLOCK/UNBLOCK DOWNLOAD       Block/unblock Vet Download
     3   EVET CHECK INCOMING       Check for incoming responses from Health eVet
     4   EVET DAILY DOWNLOAD ACTIVITY       Daily download activity for date
     5   EVET EMAIL DOWNLOAD REPORT       Email weekly download report
   Press <Enter> to see more, '^' to exit this list,  OR
   CHOOSE 1-5:
   ```

4. Select option: 1
   ```
   WARNING -- TASK MANAGER DOESN'T SEEM TO BE RUNNING!!!!

          Core Applications ...
          Device Management ...
   FM     VA FileMan ...
          Menu Management ...
          Programmer Options ...
          Operations Management ...
          Spool Management ...
          Information Security Officer Menu ...
          Taskman Management ...
          User Management ...
          Application Utilities ...
          Capacity Planning ...
          Manage Mailman ...

   <CPM> Select Systems Manager Menu <TEST ACCOUNT> Option:
   ```

5. Select `VA FileMan...` (Can be chosen by typing `FM`)
   ```
          VA FileMan Version 22.2


          Enter or Edit File Entries
          Print File Entries
          Search File Entries
          Modify File Attributes
          Inquire to File Entries
          Utility Functions ...
          Data Dictionary Utilities ...
          Transfer Entries
          Other Options ...

   <CPM> Select VA FileMan <TEST ACCOUNT> Option:
   ```

6. Select `Inquire to File Entries` (Can be chosed by typing `INQ`)
   ```
   Output from what File: OPTION//
   ```

7. If file is `OPTION` press enter, else type `OPTION` and enter
   ```
   Select OPTION NAME:
   ```

8. Type what you wish to search by. If no results found, `OPTION NAME` prompt will show again.
If results found, press enter until results are shown.
   ```
    Select OPTION NAME: DSIV
         1   DSIV DOCMANAGER       DSS DocManager Imaging System
         2   DSIV EXCEPTION REPORT       ICB Exception Report
         3   DSIV NIGHTLY REPORT       DSIV Nightly Report
         4   DSIV NON-ICB IMPORT       DSIV Non-ICB Import
    CHOOSE 1-4: 1  DSIV DOCMANAGER     DSS DocManager Imaging System
    Another one:
    Standard Captioned Output? Yes//   (Yes)
    Include COMPUTED fields:  (N/Y/R/B): NO//  - No record number (IEN), no Computed
     Fields
    
    NAME: DSIV DOCMANAGER                   MENU TEXT: DSS DocManager Imaging System
      TYPE: Broker (Client/Server)          CREATOR: PROGRAMMER,ONE
     DESCRIPTION:   This is the GUI context option for the DSS DocManager
     application.
      TIMESTAMP OF PRIMARY MENU: 58290,32371
    RPC: XWB GET VARIABLE VALUE
    RPC: DG SENSITIVE RECORD ACCESS
    RPC: DG SENSITIVE RECORD BULLETIN
    RPC: TIU LONG LIST OF TITLES
    RPC: ORWU VALIDSIG
    RPC: TIU IS THIS A CONSULT?
    RPC: TIU CREATE ADDENDUM RECORD
    RPC: TIU CREATE RECORD
    RPC: TIU UPDATE ADDITIONAL SIGNERS
    RPC: TIU UPDATE RECORD
    RPC: TIU AUTHORIZATION
    RPC: TIU SIGN RECORD
    RPC: TIU DOCUMENTS BY CONTEXT
    RPC: TIU GET RECORD TEXT
    RPC: MAG4 REMOTE IMPORT
    RPC: TIU SET ADMINISTRATIVE CLOSURE
    RPC: ORWPT ID INFO
    
    Type <Enter> to continue or '^' to exit:
    ```

## Adding context access to your user
**FROM THE VISTA COMMAND LINE**
1. `S DUZ=1`
2. `D ^XUP`
3. Option name: `EVE`
4. Select: `Systems Manager Menu` (Should be option 1)
5. Option: `USER` (Should autofill to `User Management`)
6. Option: `EDIT` (Should autofill to `Edit an Existing User`)
7. Person name: `ANRVAPPLICATION,PROXY USER` (:space:+enter will also work -- uses the last edited user)
8. Arrow down to `SECONDARY MENU OPTIONS`
9. Press enter until field becomes blank
10. Type the menu, i.e. RPC context, you wish to add (e.g. `DSIV DOCMANAGER`) and `Enter`
11. Are you adding as a new `SECONDARY MENU OPTION`? Enter `YES`
12. `Enter` through menu
13. Arrow down to command (Your new value will disappear... this is normal)
14. `Save` + `Enter`, `Exit` +  `Enter`
**You should now have access to all RPC's in the menu (context).**
