package com.github.peakz.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class VerificationDAOImp implements VerificationDAO {

	/**
	 * Get a verification for a specific match_id and captain_id.
	 *
	 * @see VerificationObject#getMatch_id()
	 * @see VerificationObject#getCaptain_id()
	 *
	 * @param match_id
	 * @param captain_id
	 * @return
	 */
	@Override
	public VerificationObject getVerification(int match_id, String captain_id) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT * FROM match_verification WHERE match_id=" + match_id + " AND captain_id=" + captain_id);

			if (rs.next()) {
				VerificationObject verification = new VerificationObject();

				verification.setVerification_id(rs.getInt("verification_id"));
				verification.setMatch_id(match_id);
				verification.setCaptain_id(captain_id);
				verification.setVerified(rs.getBoolean("verified"));

				con.close();
				return verification;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates a new verification that belongs to a specific match_id
	 * but is bound to the captains for each team in a match.
	 *
	 * Each verification has a unique id and only one captain belonging to it.
	 *
	 * @see VerificationObject#getMatch_id()
	 * @see VerificationObject#getCaptain_id()
	 *
	 * @param verification
	 */
	@Override
	public void insertVerification(VerificationObject verification) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(
					"INSERT INTO match_verification "
							+ "(match_id, "
							+ "captain_id, "
							+ "verified)"
							+ "VALUES (?, ?, ?)");

			pst.setInt(1, verification.getMatch_id());
			pst.setString(2, verification.getCaptain_id());
			pst.setBoolean(3, verification.isVerified());
			pst.executeUpdate();

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update a verification from a match through a captain from one of the teams in that match.
	 * The method updates the verification since a new verification is created one a match has started.
	 *
	 * @see VerificationObject#getMatch_id()
	 * @see VerificationObject#getCaptain_id()
	 * @see VerificationObject#isVerified()
	 *
	 * @param verification
	 */
	@Override
	public void updateVerification(VerificationObject verification) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement("UPDATE match_verification SET verified = ? WHERE match_id =" + verification.getMatch_id() + " AND captain_id=" + verification.getCaptain_id());

			pst.setBoolean(1, verification.isVerified());
			pst.executeUpdate();

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if a captain has verified the results from a match.
	 *
	 * @param captain_id
	 * @return
	 */
	@Override
	public boolean checkCaptain(String captain_id) {
		Connection con = ConnectionFactory.getConnection();
		boolean verified;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT verified FROM match_verification WHERE captain_id OR blue_captain= " + captain_id);

			if (rs.next()) {
				con.close();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}