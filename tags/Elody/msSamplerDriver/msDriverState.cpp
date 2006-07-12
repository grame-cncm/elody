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

#include <stdio.h>
#include <windows.h>

#include "MidiShare.h"
#include "FilterUtils.h"
#include "msDriverState.h"


#ifndef true
#define true 1
#define false 0
#endif

//________________________________________________________________________
static int get_private_profile_string (char* section, char* key, char* def, char* res, int size, char *fullname)
{
	return GetPrivateProfileString(section,key,def,res,size,fullname);
}

//________________________________________________________________________
static int get_private_profile_int (char* section, char* key, int def, char *fullname)
{
	return GetPrivateProfileInt(section,key,def,fullname);
}

//________________________________________________________________________
static int write_private_profile_string (char* section, char* key, char* name, char *fullname)
{
	return WritePrivateProfileString(section,key,name,fullname);
}

//________________________________________________________________________
static int write_private_profile_int (char* section, char* key, int val, char *fullname)
{
	char buffer [64];
    sprintf(buffer, "%d", val);
    return WritePrivateProfileString(section,key, buffer, fullname);
}


//________________________________________________________________________
static char * NextCnx (char *ptr, Boolean first)
{
	Boolean skipped = first;
	while (*ptr) {
		if (CnxSeparator(*ptr))	skipped = true;
		else if (skipped) return ptr;
		ptr++;
	}
	return 0;
}

//________________________________________________________________________
unsigned short CountCnx (char * cnxString)
{
	unsigned short count = 0;
	char * ptr = NextCnx (cnxString, true);
	while (ptr) {
		count++;
		ptr = NextCnx (ptr, false);
	}
	return count;
}

//________________________________________________________________________
static Boolean NullString (char * ptr)
{
	while (*ptr)
		if (*ptr++ != '0') return false;
	return true;
}

//________________________________________________________________________
short GetCnx (char * cnxString, short index)
{
	short cnx, bufsize=PortMaxEntry; char buff[PortMaxEntry];
	char * dst = buff;
	char * ptr = NextCnx (cnxString, true);
	while (index-- && ptr)
		ptr = NextCnx (ptr, false);
	if (!ptr) return CnxError;
	
	while (*ptr && !CnxSeparator(*ptr) && --bufsize)
		*dst++ = *ptr++;
	if (!bufsize) return CnxError;
	*dst = 0;
	cnx = (short)atol (buff);
	if (!cnx && !NullString (buff)) return CnxError;
	return cnx;
}


//________________________________________________________________________
static BOOL GlobalInitExist (char *fileName)
{
	char dir[512], buff[600];
	if (!GetWindowsDirectory(dir, 512))
		return FALSE;
	wsprintf (buff, "%s\\%s", dir, fileName);
	return GetFileAttributes(buff) != -1;
}

//________________________________________________________________________
char * GetProfileFullName (char * profileName)
{
	static char buff [1024];
	char dir[512];
	if (!GetCurrentDirectory(512, dir))
		return profileName;

	wsprintf (buff, "%s\\%s", dir, profileName);
	// uses local init file when it exists
	if (GetFileAttributes(buff) != -1)
		return buff;
	// uses local init file when global init file don't exist
	if (!GlobalInitExist (profileName))
		return buff;
	// no local init file and global init is present: use it
	return profileName;
}


//________________________________________________________________________
char* LoadConfig (char * section, char* key, char* fullname, char* def)
{
	static char buff[kMaxEntryLen];
        int n = get_private_profile_string (section, key, "", buff, kMaxEntryLen, fullname);
        return n ? buff : def;
}

//________________________________________________________________________
unsigned long LoadConfigNum (char * section, char* key, char* fullname, int def)
{
       return get_private_profile_int (section, key, def, fullname);
}

//________________________________________________________________________
int SaveConfig (char * section, char* key, char* name, char* fullname)
{
        return write_private_profile_string (section, key, name, fullname);
}

//________________________________________________________________________
int SaveConfigNum (char * section, char* key, int val, char* fullname)
{
       return write_private_profile_int (section, key, val, fullname);
}

//________________________________________________________________________
void LoadSlot (char * section, char* fullname, char* drivername, int slotIndex)
{
        SlotRefNum refNum = MidiGetIndSlot(MidiGetNamedAppl(drivername),slotIndex);
		
        TSlotInfos infos;
        
	  	if (MidiGetSlotInfos (refNum, &infos)) {
			char buff[kMaxEntryLen];
			unsigned long n;
			n= get_private_profile_string (section, infos.name, "", buff, kMaxEntryLen, fullname);
	          	if (n) {
				unsigned short i, c = CountCnx (buff);
				for (i=0; i<c; i++) {
					short port = GetCnx (buff, i);
					if (port != CnxError) {
						MidiConnectSlot (port, refNum, true);
					}
				}
			}
		}
}
//________________________________________________________________________
void SaveSlot (char * section, char* fullname, char* drivername, int slotIndex)
{
        SlotRefNum refNum = MidiGetIndSlot(MidiGetNamedAppl(drivername),slotIndex);
        TSlotInfos infos;
        
        if (MidiGetSlotInfos (refNum, &infos)) {
		char buff[kMaxEntryLen]; int i;
		buff[0] = 0;
                
		for (i=0; i<256; i++) {
			if (IsAcceptedBit (infos.cnx, i)) {
				char numStr[10];
				sprintf (numStr, "%d ", i);
				strcat (buff, numStr);
			}
		}
		write_private_profile_string (section, infos.name, buff, fullname);
	}
}

