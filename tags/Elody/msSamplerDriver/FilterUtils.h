/*

  Copyright © Grame 2000

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

/*
These macros can be used to access bit fields, the filter management functions
(MidiAcceptPort, MidiIsAcceptedPort...) must be used for filters.
*/


#ifndef __FilterUtils__
#define __FilterUtils__

/******************************************************************************
 *                           FILTER MACROS                                                        
 *------------------------------------------------------------------------------
 * somes macros to set and reset filter's bits. 
 *******************************************************************************/
                                                                                                                                                                                     
 #ifdef __cplusplus
 extern "C" {
	inline void     AcceptBit( char* a, Byte n)       { (a)[(n)>>3] |=  (1<<((n)&7)); }
 	inline void     RejectBit( char* a, Byte n)       { (a)[(n)>>3] &= ~(1<<((n)&7)); }
 	inline void     InvertBit( char* a, Byte n)       { (a)[(n)>>3] ^=  (1<<((n)&7)); }
 	inline Boolean  IsAcceptedBit( char* a, Byte n)   { return (a)[(n)>>3] & (1<<((n)&7)); }
 }
 #else
     	#define AcceptBit(a,n)         ( ((char*) (a))[(n)>>3] |=   (1<<((n)&7)) )
     	#define RejectBit(a,n)         ( ((char*) (a))[(n)>>3] &=  ~(1<<((n)&7)) )
     	#define InvertBit(a,n)         ( ((char*) (a))[(n)>>3] ^=   (1<<((n)&7)) )
     	#define IsAcceptedBit(a,n)     ( ((char*) (a))[(n)>>3]  &   (1<<((n)&7)) )
 #endif

#endif
