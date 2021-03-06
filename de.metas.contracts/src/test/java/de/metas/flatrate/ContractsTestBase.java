package de.metas.flatrate;

/*
 * #%L
 * de.metas.contracts
 * %%
 * Copyright (C) 2015 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */


import java.util.Date;
import java.util.Properties;

import org.adempiere.ad.trx.api.ITrx;
import org.adempiere.model.IContextAware;
import org.adempiere.model.PlainContextAware;
import org.adempiere.test.AdempiereTestHelper;
import org.adempiere.test.AdempiereTestWatcher;
import org.adempiere.util.Services;
import org.adempiere.util.time.TimeSource;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

import de.metas.flatrate.api.IContractsDAO;
import de.metas.flatrate.api.impl.PlainContractChangeDAO;
import de.metas.flatrate.api.impl.PlainContractsDAO;

public class ContractsTestBase
{
	/** Watches current test and dumps the database to console in case of failure */
	@Rule
	public final TestWatcher testWatcher = new AdempiereTestWatcher();

	@BeforeClass
	public static void staticInit()
	{
		AdempiereTestHelper.get().staticInit();
	}

	protected IContextAware context;
	protected PlainContractsDAO dao;
	protected PlainContractChangeDAO contractChangeDAO;

	@Before
	public final void beforeTest()
	{
		AdempiereTestHelper.get().init();

		dao = (PlainContractsDAO)Services.get(IContractsDAO.class);

		//
		// Setup context
		final Properties ctx = Env.getCtx();
		ctx.clear();
		Env.setContext(ctx, "#AD_Client_ID", 1);
		Env.setContext(ctx, "#AD_Org_ID", 1);
		Env.setContext(ctx, "#AD_Role_ID", 1);
		Env.setContext(ctx, "#AD_User_ID", 1);

		context = new PlainContextAware(ctx, ITrx.TRXNAME_None);

		init();
	}

	protected void init()
	{
		// nothing
	}

	protected IContextAware getContext()
	{
		return context;
	}

	public static class FixedTimeSource implements TimeSource
	{
		private final Date date;

		public FixedTimeSource(int year, int month, int day)
		{
			this(TimeUtil.getDay(year, month, day));
		}

		public FixedTimeSource(Date date)
		{
			this.date = date;
		}

		@Override
		public long millis()
		{
			return date.getTime();
		}
	}

}
