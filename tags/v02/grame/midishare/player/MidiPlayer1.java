package grame.midishare.player;

import grame.midishare.Midi;
import grame.midishare.SmpteLoc;
import grame.midishare.midifile.MidiFileInfos;

/**
 * This class in an interface to the native Player library. It allows to develop
 * multi-tracks, synchronisable MidiShare players. Each Player is a MidiShare
 * application, has 256 tracks, four different synchronization mode, events
 * chase and loop markers.
 * 
 */

public final class MidiPlayer1 {

	private static boolean interfaceLoaded = false;

	/* Don't let anyone instantiate this class. */
	private MidiPlayer1() {
	}

	/** Player status. */
	public static final int kIdle = 0;

	/** Player status. */
	public static final int kPause = 1;

	/** Player status. */
	public static final int kRecording = 2;

	/** Player status. */
	public static final int kPlaying = 3;

	/** Player status. */
	public static final int kWaiting = 4;

	/** Maximum track value = 256. */
	public static final int kMaxTrack = 256;

	/** Track state. */
	public static final int kMuteOn = 1;

	/** Track state. */
	public static final int kMuteOff = 0;

	/** Track state. */
	public static final int kSoloOn = 1;

	/** Track state. */
	public static final int kSoloOff = 0;

	/** Track state. */
	public static final int kMute = 0;

	/** Track state. */
	public static final int kSolo = 1;

	/** For recording management. */
	public static final int kNoTrack = -1;

	/** For recording management. */
	public static final int kEraseMode = 1;

	/** For recording management. */
	public static final int kMixMode = 0;

	/** For loop management. */
	public static final int kLoopOn = 0;

	/** For loop management. */
	public static final int kLoopOff = 1;

	/** For step playing management. */
	public static final int kStepPlay = 1;

	/** For step playing management. */
	public static final int kStepMute = 0;

	/** For synchronisation management. */
	public static final int kInternalSync = 0;

	/** For synchronisation management. */
	public static final int kClockSync = 1;

	/** For synchronisation management. */
	public static final int kSMPTESync = 2;

	/** For synchronisation management. */
	public static final int kExternalSync = 3;

	/** For synchronisation management. */

	public static final int kNoSyncOut = 0;

	/** For synchronisation management. */
	public static final int kClockSyncOut = 1;

	/** Error code : no error. */
	public static final int PLAYERnoErr = -1;

	/** Error code : Unable to open MidiShare application. */
	public static final int PLAYERerrAppl = -2;

	/** Error code : No more MidiShare Memory. */
	public static final int PLAYERerrEvent = -3;

	/** Error code : No more Memory. */
	public static final int PLAYERerrMemory = -4;

	/** Error code : Sequencer error. */
	public static final int PLAYERerrSequencer = -5;

	// for MIDIFile

	static final int noErr = 0; /* no error */

	static final int ErrOpen = 1; /* file open error */

	static final int ErrRead = 2; /* file read error */

	static final int ErrWrite = 3; /* file write error */

	static final int ErrMidiFileFormat = 7; /* bad MidiFile format */

	// Player allocation

	/**
	 * Open a new empty Player. The tempo is assumed to be 120 bpm, the time
	 * signature is 4/4 and the internal resolution is set by default to 500
	 * ticks_per_quarter so that the internal time unit (tick) correspond
	 * exactly to one millisecond. Any opened Player should be closed using the
	 * Close function.
	 * 
	 * @param name
	 *            is the name of the Player (actually the name of the associated
	 *            MidiShare application)
	 * @return The result is the Player reference number (actually the reference
	 *         number of the associated MidiShare application)
	 */

	public static final int Open(String name) {
		int cstr = Midi.ConvertJavaString(name);
		int res = OpenAux(cstr);
		Midi.FreeString(cstr);
		return res;
	}

	private static final native int OpenAux(int name);

	/**
	 * Close a Player given it's reference number. This function automatically
	 * frees the internal score and close the MidiShare application.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * 
	 */

	public static final native void Close(int refnum);

	// Transport control

	/**
	 * Start a Player from the beginning of the score.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * 
	 */

	public static final native void Start(int refnum);

	/**
	 * Start a Player from the current position in the score.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * 
	 */

	public static final native void Cont(int refnum);

	/**
	 * Stop a Player without sending the chased events (key-off ...)
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * 
	 */

	public static final native void Pause(int refnum);

	/**
	 * Stop a Player and send the chased events (key-off ...)
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * 
	 */

	public static final native void Stop(int refnum);

	// Record management

	/**
	 * Set the recording mode. A recording can be done in two mode : <br> -
	 * kMixMode : recorded events are mixed in the recorded track. <br> -
	 * kEraseMode : the track is deleted between punch in/out points and
	 * recorded events are then inserted in the track. By default the recording
	 * state is set in kMixMode.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param state
	 *            is the recording state : can be kEraseMode or kMixMode.
	 */

	public static final native void SetRecordMode(int refnum, int state);

	/**
	 * Set a track in record mode. A Player can record in one track at a time.
	 * RecordPlayer called with kNoTrack parameter will disable all recording.
	 * The track being recorded can be changed while recording.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param tracknum
	 *            the number of the track to be set in record mode: from 0 to
	 *            255. Parameter kNoTrack will switch off all recording.
	 */

	public static final native void Record(int refnum, int tracknum);

	/**
	 * Allows to set a record filter for the Player. Midi events can be filtered
	 * by type, port, and channel.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param filter
	 *            is a pointer to a Midi filter.
	 * @see grame.midishare.Midi#NewFilter
	 * @see grame.midishare.Midi#FreeFilter
	 * @see grame.midishare.Midi#AcceptChan
	 * @see grame.midishare.Midi#AcceptPort
	 * @see grame.midishare.Midi#AcceptType
	 */

	public static final native void SetRecordFilter(int refnum, int filter);

	// Position management

	/**
	 * Change the current position in the score. The new position is given as a
	 * position in musical time (Bar, Beat, Unit). This function can be used
	 * when the Player is moving but only is kInternalSync mode.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param pos
	 *            is a PlayerPos object.
	 */

	public static final native void SetPosBBU(int refnum, PlayerPos pos);

	/**
	 * Change the current position in the score. The new position is given as a
	 * date in millisecond. This function can be used when the Player is moving
	 * but only is kInternalSync mode.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param date
	 *            is a date in millisecond.
	 */

	public static final native void SetPosMs(int refnum, int date_ms);

	/**
	 * Change the current position in the score. The new position is given as a
	 * date in ticks. This function can be used when the Player is moving but
	 * only is kInternalSync mode.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param date
	 *            is a date in ticks.
	 */

	public static final native void SetPosTicks(int refnum, int date_ticks);

	// Loop management

	/**
	 * Switch on/off the loop state for the Player.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param state
	 *            can be kLoopOn or kLoopOff.
	 */

	public static final native void SetLoop(int refnum, int state);

	/**
	 * Set the position of the loop start marker with a position given in
	 * musical time (Bar, Beat, Unit). This function can be used when the Player
	 * is moving but only in kInternalSync mode.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param pos
	 *            is a PlayerPos object.
	 */

	public static final native int SetLoopStartBBU(int refnum, PlayerPos pos);

	/**
	 * Set the position of the loop end marker with a position given in musical
	 * time (Bar, Beat, Unit). This function can be used when the Player is
	 * moving but only in kInternalSync mode.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param pos
	 *            is a PlayerPos object.
	 * @return The result is an error code. This error is returned when trying
	 *         to set a Loop end marker before a the Loop start marker or a Loop
	 *         start after a Loop end marker.
	 */

	public static final native int SetLoopEndBBU(int refnum, PlayerPos pos);

	/**
	 * Set the position of the loop start marker with a date given in
	 * millisecond. This function can be used when the Player is moving but only
	 * is kInternalSync mode.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param date
	 *            is a date in millisecond.
	 * @return The result is an error code. This error is returned when trying
	 *         to set a Loop end marker before a the Loop start marker or a Loop
	 *         start after a Loop end marker.
	 */

	public static final native int SetLoopStartMs(int refnum, int date_ms);

	/**
	 * Set the position of the loop end marker with a date given in millisecond.
	 * This function can be used when the Player is moving but only is
	 * kInternalSync mode.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param date
	 *            is a date in millisecond.
	 * @return The result is an error code. This error is returned when trying
	 *         to set a Loop end marker before a the Loop start marker or a Loop
	 *         start after a Loop end marker.
	 */

	public static final native int SetLoopEndMs(int refnum, int date_ms);

	// Synchronisation management

	/**
	 * Set the Player mode of synchronization, a Player can be in four
	 * synchronization mode : <br> - kInternalSync : the Player uses its
	 * internal tempo Map. <br> - kClockSync : The Player recognizes MIDI Clock
	 * at it's input and can be driven by Start, Stop, Continue, Clock and
	 * SongPos Midi messages. <br> - kSMPTESync : This is a global
	 * Synchronization mode for all MidiShare applications. <br> - kExternalSync :
	 * The Player tempo can be changed using the SetTempo function or when
	 * external Tempo events are received. <br>
	 * The synchronization mode can be changed only when the Player is stopped.
	 * By default, the synchronization mode is kInternalSync.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param state
	 *            can be kInternalSync, kClockSync, kSMPTEsync or kExternalSync.
	 */

	public static final native void SetSynchroIn(int refnum, int state);

	/**
	 * Set the Player mode of synchronization sending, which can be kNoSyncOut
	 * or kClockSyncOut. In this case start, stop, continue, and clock Midi
	 * messages are sent when the Player is running. By default, the
	 * synchronization mode is kNoSyncOut.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param state
	 *            can be kNoSyncOut or kClockSyncOut.
	 */

	public static final native void SetSynchroOut(int refnum, int state);

	/**
	 * Allows to change the current tempo when the Player is in kExternalSync
	 * mode. The tempo is in micro-second/per/quarter-note. (tempo in
	 * micro-second/per/quarter-note = 60 000 000 / tempo in beat/per/min)
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param tempo
	 *            is the new tempo value to be set.
	 */

	public static final native void SetTempo(int refnum, int tempo);

	/**
	 * Allows to change the current tempo factor when the Player is in
	 * kExternalSync mode. The tempo factor is a float value.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param factor
	 *            is the new factor value to be set.
	 */

	public static final native void SetTempoFactor(int refnum, float tempo);

	/**
	 * For SMPTE synchronization mode, allows to set an offset.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param smptepos
	 *            a SmpteLoc object.
	 */

	public static final native void SetSMPTEOffset(int refnum, SmpteLoc smptepos);

	// State management

	/**
	 * Returns the state of the Player at the current position. This function is
	 * usually used for state displaying purpose.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param state
	 *            a PlayerState object, will be filled with the state of the
	 *            Player.
	 */

	public static final native void GetState(int refnum, PlayerState playerstate);

	/**
	 * Returns information about the last event in the score: position in
	 * musical time and millisecond...
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param state
	 *            a PlayerState object, will be filled with information about
	 *            the last event in the score : position in (Bar, Beat,Unit) and
	 *            millisecond.
	 */

	public static final native void GetEndScore(int refnum,
			PlayerState playerstate);

	// Step playing

	/**
	 * This function allows to implement step playing. ForwardStep plays the
	 * next group of notes (notes at the same date) and allows to move in the
	 * score by group of notes. ForwardStepPlayer takes care of the tracks state
	 * (mute or solo mode).
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param state
	 *            can be kStepPlay or kStepMute : event will be played or not.
	 */

	public static final native void ForwardStep(int refnum, int flag);

	/**
	 * This function allows to implement step playing. BackwardStep plays the
	 * previous group of notes (notes at the same date) and allows to move in
	 * the score by group of notes. BackwardStepPlayer takes care of the tracks
	 * state (mute or solo mode).
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param state
	 *            can be kStepPlay or kStepMute : event will be played or not.
	 */

	public static final native void BackwardStep(int refnum, int flag);

	// Tracks management

	/**
	 * Returns all the tracks contained in a Player as a MidiShare sequence. All
	 * tracks are mixed in a unique MidiShare sequence but are distinguish by
	 * the reference number of their events. The returned sequence is a COPY of
	 * the internal score.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @return The result is a MidiShare sequence where all tracks are mixed.
	 */

	public static final native int GetAllTrack(int refnum);

	/**
	 * Returns a track contained in a Player as a MidiShare sequence given the
	 * tracknumber. The returned sequence is a COPY of the internal track.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param tracknum
	 *            is the track number.
	 * @return The result is MidiShare sequence.
	 */

	public static final native int GetTrack(int refnum, int tracknum);

	/**
	 * Replace a track in a Player with a new MidiShare sequence. The existing
	 * track will be erased before the new track is set. The MidiShare sequence
	 * given as parameter will be internally used. It means that one must copy
	 * it before using SetTrack if one wants to keep it.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param tracknum
	 *            is the track number.
	 * @param seq
	 *            is a pointer on a sequence to be set in the Player.
	 * @return The result is an error code. This error is returned if the
	 *         SetTrack function is used when the Player is running or if the
	 *         sequence could not be inserted.
	 */

	public static final native int SetTrack(int refnum, int tracknum, int seq);

	/**
	 * Replace all tracks of a Player with a new MidiShare sequence. All tracks
	 * are mixed in a unique MidiShare sequence and should be distinguish by the
	 * value of the refnum field of the Midi events . The MidiShare sequence
	 * given as parameter will be internally used. It means that one must copy
	 * it before using SetAllTrack if one wants to keep it.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param seq
	 *            is a pointer on a sequence to be set in the Player.
	 * @param tick_per_quarter :
	 *            the number of ticks per quarter note, usually read in the
	 *            MIDIfile header.
	 * @return The result is an error code. This error is returned if the
	 *         SetAllTrack function is used when the Player is running or if the
	 *         sequence could not be inserted.
	 */

	public static final native int SetAllTrack(int refnum, int seq,
			int ticks_per_quarter);

	/**
	 * Set parameters which define the behavior of a track: a track can be muted
	 * or played solo.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param tracknum
	 *            is the track number to be set.
	 * 
	 * @param param
	 *            is the parameter to be set (kMute or kSolo).
	 * @param val
	 *            is tha value to be set, can be kMuteOn, kMuteOff, kSoloOn or
	 *            kSoloOff.
	 */
	public static final native void SetParam(int refnum, int tracknum,
			int param, int value);

	/**
	 * Returns the current value of the parameter solo or mute in a track.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param tracknum
	 *            is the track number to be set.
	 * 
	 * @param param
	 *            is the parameter to be read (kMute or kSolo).
	 * @return The result is the value of the corresponding parameter.
	 */

	public static final native int GetParam(int refnum, int tracknum, int param);

	/**
	 * Insert a score "slice" in the Player. All tracks in the slice are mixed
	 * in a unique MidiShare sequence and should be distinguish by the value of
	 * the refnum field of the Midi events. The MidiShare sequence given as
	 * parameter will be internally used. It means that one must copy it before
	 * using InsertAllTrack if one wants to keep it.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param seq
	 *            is a pointer on a sequence to be inserted in the Player.
	 * @return The result is an error code. This error is returned if the score
	 *         "slice" can not be inserted.
	 */

	public static final native int InsertAllTrack(int refnum, int seq);

	/**
	 * Insert a track "slice" in the Player. The MidiShare sequence given as
	 * parameter will be internally used. It means that one must copy it before
	 * using InsertTrack if one wants to keep it.
	 * 
	 * @param refnum
	 *            is the Player reference number.
	 * @param tracknum
	 *            is the track number.
	 * @param seq
	 *            is a pointer on a sequence to be inserted in the Player.
	 * @return The result is an error code. This error is returned if the track
	 *         "slice" can not be inserted.
	 */

	public static final native int InsertTrack(int refnum, int tracknum, int seq);

	static final native int Load(String name, int sequence, MidiFileInfos info);

	static final native int Save(String name, int sequence, MidiFileInfos info);

	static {
		if (!interfaceLoaded) {
			try {
				System.loadLibrary("JPlayer1");
				interfaceLoaded = true;
			} catch (UnsatisfiedLinkError e) {
				System.err.println("JPlayer1.library not found");
			}
		}
	}

}