###
# ============LICENSE_START=======================================================
# ONAP Policy Engine
# ================================================================================
# Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
# ================================================================================
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#      http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ============LICENSE_END=========================================================
###

#!/bin/bash
#
#	certtool [ init fqdn | importcert cert.cer.txt | exportcsr ]
#

KEYPASS=${KEYSTORE_PASSWD}
STOREPASS=${KEYSTORE_PASSWD}
DIR=${POLICY_HOME}/etc/ssl

KSFILE=policy-keystore
ID=`id -n -u`
GRP=`id -n -g`

if [ ! -d $DIR ]
then
	echo "Policy Engine application software must be installed before using certtool"
	exit 1
fi

TZ=GMT0
umask 0077
cd $DIR

MODE="$1"
shift
if [ "$MODE" = init ]
then
	if [ -f $KSFILE ]
	then
		echo "File \"$KSFILE\" already exists - did you already initialize?"
		echo 'Remove it before initializing'
		exit 1
	fi
	
	if [ -f $KSFILE.tmp ]
	then
		echo "File \"$KSFILE.tmp\" already exists - did you already initialize?"
		echo 'Remove it before initializing'
		exit 1
	fi
	
	rm -f $KSFILE.csr
	FQDN="$1"
	shift
	if [ "$FQDN" = "" ]
	then
		echo 'FQDN of server required for certtool init'
		exit 1
	fi

	$JAVA_HOME/bin/keytool -genkey -alias $FQDN -keyalg RSA -keystore $KSFILE.tmp -keysize 2048 -storepass "$STOREPASS" -keypass "$KEYPASS" -dname "CN=$FQDN,OU=Information Technology,O=AT&T Services\, Inc.,L=Southfield,S=Michigan,C=US"
	$JAVA_HOME/bin/keytool -certreq -alias $FQDN -keystore $KSFILE.tmp -file $KSFILE.csr -storepass "$STOREPASS" -keypass "$KEYPASS"
	echo cat $DIR/$KSFILE.csr
	cat $KSFILE.csr
	echo Keystore initialized.  Use the above certificate signing request.
	exit 0
fi

if [ "$MODE" != "importcert" -a "$MODE" != "exportcsr" ]
then
	echo "Improper arguments.  Usage is:"
	echo "First time - to create key pair:"
	echo " certtool init <fqdn>"
	echo "Install certificate file:"
	echo " certtool importcert <cert.cer.txt>"
	echo "Generate certificate signing request when old certificate nears expiry:"
	echo " certtool exportcsr"
	exit 1
fi

KS=$KSFILE
if [ ! -f $KSFILE ]
then
	KS=$KSFILE.tmp
	if [ ! -f $KSFILE.tmp ]
	then
		echo "Keystore not initialized."
		exit 1
	fi
fi

$JAVA_HOME/bin/keytool -list -keystore $KS -storepass "$STOREPASS" | grep ', PrivateKeyEntry, $'
FQDN=`$JAVA_HOME/bin/keytool -list -keystore $KS -storepass "$STOREPASS" | grep ', PrivateKeyEntry, $' | sed 's/,.*//'`
if [ "$FQDN" = "" ]
then
	echo "Unable to read keystore file $KS."
	exit 1
fi

if [ "$MODE" = exportcsr ]
then
	if [ ! -f $KSFILE ]
	then
		echo "Cannot export new signing request before initial certificate imported"
		exit 1
	fi
	rm -f $KSFILE.csr
	$JAVA_HOME/bin/keytool -certreq -alias $FQDN -keystore $KS -file $KSFILE.csr -storepass "$STOREPASS" -keypass "$KEYPASS"
	echo cat $DIR/$KSFILE.csr
	cat $KSFILE.csr
	echo Use the above certificate signing request.
	exit 1
fi

FN="$1"
shift
cd -

if [ ! -f "$FN" ]
then
	echo "Certificate file $FN not found."
	exit 1
fi
XFN=$DIR/$$.cer
cp "$FN" $XFN
cat <<!EOF >> $XFN
-----BEGIN CERTIFICATE-----
MIIFODCCBCCgAwIBAgIQUT+5dDhwtzRAQY0wkwaZ/zANBgkqhkiG9w0BAQsFADCB
yjELMAkGA1UEBhMCVVMxFzAVBgNVBAoTDlZlcmlTaWduLCBJbmMuMR8wHQYDVQQL
ExZWZXJpU2lnbiBUcnVzdCBOZXR3b3JrMTowOAYDVQQLEzEoYykgMjAwNiBWZXJp
U2lnbiwgSW5jLiAtIEZvciBhdXRob3JpemVkIHVzZSBvbmx5MUUwQwYDVQQDEzxW
ZXJpU2lnbiBDbGFzcyAzIFB1YmxpYyBQcmltYXJ5IENlcnRpZmljYXRpb24gQXV0
aG9yaXR5IC0gRzUwHhcNMTMxMDMxMDAwMDAwWhcNMjMxMDMwMjM1OTU5WjB+MQsw
CQYDVQQGEwJVUzEdMBsGA1UEChMUU3ltYW50ZWMgQ29ycG9yYXRpb24xHzAdBgNV
BAsTFlN5bWFudGVjIFRydXN0IE5ldHdvcmsxLzAtBgNVBAMTJlN5bWFudGVjIENs
YXNzIDMgU2VjdXJlIFNlcnZlciBDQSAtIEc0MIIBIjANBgkqhkiG9w0BAQEFAAOC
AQ8AMIIBCgKCAQEAstgFyhx0LbUXVjnFSlIJluhL2AzxaJ+aQihiw6UwU35VEYJb
A3oNL+F5BMm0lncZgQGUWfm893qZJ4Itt4PdWid/sgN6nFMl6UgfRk/InSn4vnlW
9vf92Tpo2otLgjNBEsPIPMzWlnqEIRoiBAMnF4scaGGTDw5RgDMdtLXO637QYqzu
s3sBdO9pNevK1T2p7peYyo2qRA4lmUoVlqTObQJUHypqJuIGOmNIrLRM0XWTUP8T
L9ba4cYY9Z/JJV3zADreJk20KQnNDz0jbxZKgRb78oMQw7jW2FUyPfG9D72MUpVK
Fpd6UiFjdS8W+cRmvvW1Cdj/JwDNRHxvSz+w9wIDAQABo4IBYzCCAV8wEgYDVR0T
AQH/BAgwBgEB/wIBADAwBgNVHR8EKTAnMCWgI6Ahhh9odHRwOi8vczEuc3ltY2Iu
Y29tL3BjYTMtZzUuY3JsMA4GA1UdDwEB/wQEAwIBBjAvBggrBgEFBQcBAQQjMCEw
HwYIKwYBBQUHMAGGE2h0dHA6Ly9zMi5zeW1jYi5jb20wawYDVR0gBGQwYjBgBgpg
hkgBhvhFAQc2MFIwJgYIKwYBBQUHAgEWGmh0dHA6Ly93d3cuc3ltYXV0aC5jb20v
Y3BzMCgGCCsGAQUFBwICMBwaGmh0dHA6Ly93d3cuc3ltYXV0aC5jb20vcnBhMCkG
A1UdEQQiMCCkHjAcMRowGAYDVQQDExFTeW1hbnRlY1BLSS0xLTUzNDAdBgNVHQ4E
FgQUX2DPYZBV34RDFIpgKrL1evRDGO8wHwYDVR0jBBgwFoAUf9Nlp8Ld7LvwMAnz
Qzn6Aq8zMTMwDQYJKoZIhvcNAQELBQADggEBAF6UVkndji1l9cE2UbYD49qecxny
H1mrWH5sJgUs+oHXXCMXIiw3k/eG7IXmsKP9H+IyqEVv4dn7ua/ScKAyQmW/hP4W
Ko8/xabWo5N9Q+l0IZE1KPRj6S7t9/Vcf0uatSDpCr3gRRAMFJSaXaXjS5HoJJtG
QGX0InLNmfiIEfXzf+YzguaoxX7+0AjiJVgIcWjmzaLmFN5OUiQt/eV5E1PnXi8t
TRttQBVSK/eHiXgSgW7ZTaoteNTCLD0IX4eRnh8OsN4wUmSGiaqdZpwOdgyA8nTY
Kvi4Os7X1g8RvmurFPW9QaAiY4nxug9vKWNmLT+sjHLF+8fk1A/yO0+MKcc=
-----END CERTIFICATE-----
-----BEGIN CERTIFICATE-----
MIIE0DCCBDmgAwIBAgIQJQzo4DBhLp8rifcFTXz4/TANBgkqhkiG9w0BAQUFADBf
MQswCQYDVQQGEwJVUzEXMBUGA1UEChMOVmVyaVNpZ24sIEluYy4xNzA1BgNVBAsT
LkNsYXNzIDMgUHVibGljIFByaW1hcnkgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkw
HhcNMDYxMTA4MDAwMDAwWhcNMjExMTA3MjM1OTU5WjCByjELMAkGA1UEBhMCVVMx
FzAVBgNVBAoTDlZlcmlTaWduLCBJbmMuMR8wHQYDVQQLExZWZXJpU2lnbiBUcnVz
dCBOZXR3b3JrMTowOAYDVQQLEzEoYykgMjAwNiBWZXJpU2lnbiwgSW5jLiAtIEZv
ciBhdXRob3JpemVkIHVzZSBvbmx5MUUwQwYDVQQDEzxWZXJpU2lnbiBDbGFzcyAz
IFB1YmxpYyBQcmltYXJ5IENlcnRpZmljYXRpb24gQXV0aG9yaXR5IC0gRzUwggEi
MA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCvJAgIKXo1nmAMqudLO07cfLw8
RRy7K+D+KQL5VwijZIUVJ/XxrcgxiV0i6CqqpkKzj/i5Vbext0uz/o9+B1fs70Pb
ZmIVYc9gDaTY3vjgw2IIPVQT60nKWVSFJuUrjxuf6/WhkcIzSdhDY2pSS9KP6HBR
TdGJaXvHcPaz3BJ023tdS1bTlr8Vd6Gw9KIl8q8ckmcY5fQGBO+QueQA5N06tRn/
Arr0PO7gi+s3i+z016zy9vA9r911kTMZHRxAy3QkGSGT2RT+rCpSx4/VBEnkjWNH
iDxpg8v+R70rfk/Fla4OndTRQ8Bnc+MUCH7lP59zuDMKz10/NIeWiu5T6CUVAgMB
AAGjggGbMIIBlzAPBgNVHRMBAf8EBTADAQH/MDEGA1UdHwQqMCgwJqAkoCKGIGh0
dHA6Ly9jcmwudmVyaXNpZ24uY29tL3BjYTMuY3JsMA4GA1UdDwEB/wQEAwIBBjA9
BgNVHSAENjA0MDIGBFUdIAAwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cudmVy
aXNpZ24uY29tL2NwczAdBgNVHQ4EFgQUf9Nlp8Ld7LvwMAnzQzn6Aq8zMTMwbQYI
KwYBBQUHAQwEYTBfoV2gWzBZMFcwVRYJaW1hZ2UvZ2lmMCEwHzAHBgUrDgMCGgQU
j+XTGoasjY5rw8+AatRIGCx7GS4wJRYjaHR0cDovL2xvZ28udmVyaXNpZ24uY29t
L3ZzbG9nby5naWYwNAYIKwYBBQUHAQEEKDAmMCQGCCsGAQUFBzABhhhodHRwOi8v
b2NzcC52ZXJpc2lnbi5jb20wPgYDVR0lBDcwNQYIKwYBBQUHAwEGCCsGAQUFBwMC
BggrBgEFBQcDAwYJYIZIAYb4QgQBBgpghkgBhvhFAQgBMA0GCSqGSIb3DQEBBQUA
A4GBABMC3fjohgDyWvj4IAxZiGIHzs73Tvm7WaGY5eE43U68ZhjTresY8g3JbT5K
lCDDPLq9ZVTGr0SzEK0saz6r1we2uIFjxfleLuUqZ87NMwwq14lWAyMfs77oOghZ
tOxFNfeKW/9mz1Cvxm1XjRl4t7mi0VfqH5pLr7rJjhJ+xr3/
-----END CERTIFICATE-----
!EOF
cd $DIR
$JAVA_HOME/bin/keytool -import -trustcacerts -alias $FQDN -keystore $KS -storepass "$STOREPASS" -keypass "$KEYPASS" -file $XFN
rm -f $XFN

if [ "$KS" = "$KSFILE.tmp" ]
then
	mv $KSFILE.tmp $KSFILE
fi

rm -f $KSFILE.csr
echo Certificate imported.
exit 0
