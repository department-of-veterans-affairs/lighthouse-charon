# Authorization Status

The Charon API provides a specialized endpoint for checking authorization for a particular user. This endpoint is not
general purpose but can be used to verify that a user has access to a particular menu option in Vista.

Endpoint

```
GET /authorization-status/clinical?site={vista}&duz={user-duz}
```

Query Parameters

```
site ......... (required) The vista site, either by name or coordinates
duz .......... (required) The users DUZ
menu-option .. (optional) The menu option to check for
               If not specified a default configured menu option will be used
               (OR CPRS GUI CHART)
```

Reponse

- `200` `ok`
- `401` `unauthorized`
- `403` `forbidden`
- `500` _variable error message_

```
{ 
  "status":"<http-status-name>"
  "value":"<menu-option-value>" 
}
```

Menu option values are defined as:

```
-1: no such user in the New Person File.
-2: User terminated or has no access code.
-3: no such option in the Option File.
 0: no access found in any menu tree the user owns.
 n: Positive cases are access allowed.
```

## Under the hood

This endpoint uses Lighthouse specific Vista packages to verify the user for the given DUZ has access to a particular
menu option.

Requires

- RPC `LHS CHECK OPTION ACCESS`
- RPC Context `LHS RPC CONTEXT`
- Application proxy user configuration that can use the above RPC

Configuration

```
clinical-authorization-status.default-menu-option=OR CPRS GUI CHART
clinical-authorization-status.access-code
clinical-authorization-status.verify-code
clinical-authorization-status.application-proxy-user
```

## Test environments and alternate IDs

To support test environments made of disparate test systems, e.g. SSOi, Vista sites, MPI, etc., the Authorization Status
endpoint can be configured to perform alternate ID substitution on a limited set of ID/Site combinations. This allows a
source system to request an authorization check for one DUZ at a test Vista that it is aware of, and have the
authorization check be made for a different DUZ at a different site that the test Charon instance is aware of.

This is a complex topic with subtle nuances of different environments that necessitate the capability.
See https://github.com/department-of-veterans-affairs/health-apis-clinical-fhir for a specific use case.

#### Configuration

Additional configuration properties must be specified to enable Alternate IDs.

Support must be explicitly enabled. This will add special components to the Spring context that would otherwise be
omitted. These components make alternate IDs possible.

```
alternate-authorization-status-ids.enabled=false
```

The limited set of alternate IDs must also be specified as a list.

```
alternate-authorization-status-ids.ids=alternate,alternate,...
```

Where `alternate` is defined as `publicDuz1@siteA:privateDuz1@siteB`. This tells Charon to process request.

`/authorization-status/clinical?site=siteA&duz=publicDuz1`

as if it were

`/authorization-status/clinical?site=siteB&duz=privateDuz1`