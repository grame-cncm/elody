/*

  Copyright © Grame 2002

  This library is free software; you can redistribute it and modify it under 
  the terms of the GNU Library General Public License as published by the 
  Free Software Foundation version 2 of the License, or any later version.

  This library is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Library General Public License 
  for more details.

  You should have received a copy of the GNU Library General Public License
  along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

  Grame Research Laboratory, 9, rue du Garet 69001 Lyon - France
  grame@rd.grame.fr

*/

#define MidiShareDirectory "MidiShare"

#define kMaxEntryLen	1024
#define PortMaxEntry	10
#define CnxError	-1

static __inline Boolean CnxSeparator(int c) { return ((c)==' ') || ((c)=='	'); }

unsigned short CountCnx (char * cnxString);
short GetCnx (char * cnxString, short index);

char * GetProfileFullName (char * name);
char* LoadConfig (char * section, char* key, char* fullname, char* def);
unsigned long LoadConfigNum (char * section, char* key, char* fullname, int def);
int SaveConfig (char * section, char* key, char* name, char* fullname);
int SaveConfigNum (char * section, char* key, int val, char* fullname);
void LoadSlot (char * section, char* fullname, char* drivername, int slotIndex);
void SaveSlot (char * section, char* fullname,char* drivername, int slotIndex);
