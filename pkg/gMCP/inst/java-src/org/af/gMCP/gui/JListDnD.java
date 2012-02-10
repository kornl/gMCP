package org.af.gMCP.gui;

/* Based on JListDnDFun.java, Copyright 2009 Sebastian Haufe

 * Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.DefaultListModel;
//import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;

public class JListDnD extends JList {

	public JListDnD(ListModel dataModel) {
		super(dataModel);
		setDragEnabled(true);
		//TODO Uncomment
		//setDropMode(DropMode.INSERT);
		setTransferHandler(new ListMoveTransferHandler());
	}

	/**
	 * Model bound data flavor.
	 */
	static class ListMoveDataFlavor extends DataFlavor {

		private final DefaultListModel model;

		public ListMoveDataFlavor(DefaultListModel model) {
			super(ListMoveTransferData.class, "List Data");
			this.model = model;
		}

		public DefaultListModel getModel() {
			return model;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((model == null) ? 0 : model.hashCode());
			return result;
		}

		@Override
		public boolean equals(DataFlavor that) {
			if (this == that) {
				return true;
			}
			if (!super.equals(that) || getClass() != that.getClass()) {
				return false;
			}
			return match(model, that);
		}

		/**
		 * Tests whether the given data flavor is a {@link ListMoveDataFlavor} and
		 * matches the given model.
		 * 
		 * @param model the model
		 * @param flavor the flavor
		 * @return {@code true} if matches
		 */
		public static boolean match(DefaultListModel model, DataFlavor flavor) {
			return flavor instanceof ListMoveDataFlavor
			&& ((ListMoveDataFlavor) flavor).getModel() == model;
		}
	}

	/**
	 * Model bound and index based transfer data.
	 */
	private static class ListMoveTransferData {

		private final DefaultListModel model;
		private final int[] indices;

		ListMoveTransferData(DefaultListModel model, int[] indices) {
			this.model = model;
			this.indices = indices;
		}

		int[] getIndices() {
			return indices;
		}

		public DefaultListModel getModel() {
			return model;
		}
	}

	/**
	 * Model bound transferable implementation.
	 */
	static class ListMoveTransferable implements Transferable {

		private final ListMoveTransferData data;

		public ListMoveTransferable(ListMoveTransferData data) {
			this.data = data;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { new ListMoveDataFlavor(data.getModel()) };
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return ListMoveDataFlavor.match(data.getModel(), flavor);
		}

		public Object getTransferData(DataFlavor flavor)
		throws UnsupportedFlavorException, IOException {
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return data;
		}
	}

	/**
	 * List transfer handler.
	 */
	static class ListMoveTransferHandler extends TransferHandler {

		@Override
		public int getSourceActions(JComponent c) {
			final JList list = (JList) c;
			return list.getModel() instanceof DefaultListModel ? MOVE : NONE;
		}

		@Override
		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			if (!(comp instanceof JList)
					|| !(((JList) comp).getModel() instanceof DefaultListModel)) {
				return false;
			}

			final DefaultListModel model =
				(DefaultListModel) ((JList) comp).getModel();
			for (DataFlavor f : transferFlavors) {
				if (ListMoveDataFlavor.match(model, f)) {
					return true;
				}
			}
			return false;
		}

		@Override
		protected Transferable createTransferable(JComponent c) {
			final JList list = (JList) c;
			final int[] selectedIndices = list.getSelectedIndices();
			return new ListMoveTransferable(new ListMoveTransferData(
					(DefaultListModel) list.getModel(), selectedIndices));
		}

		//TODO Uncomment
	/*	@Override
		public boolean importData(TransferHandler.TransferSupport info) {
			final Component comp = info.getComponent();
			if (!info.isDrop() || !(comp instanceof JList)) {
				return false;
			}
			final JList list = (JList) comp;
			final ListModel lm = list.getModel();
			if (!(lm instanceof DefaultListModel)) {
				return false;
			}

			final DefaultListModel listModel = (DefaultListModel) lm;
			final DataFlavor flavor = new ListMoveDataFlavor(listModel);
			if (!info.isDataFlavorSupported(flavor)) {
				return false;
			}

			final Transferable transferable = info.getTransferable();
			try {
				final ListMoveTransferData data =
					(ListMoveTransferData) transferable.getTransferData(flavor);

				// get the initial insertion index
				final JList.DropLocation dropLocation = list.getDropLocation();
				int insertAt = dropLocation.getIndex();

				// get the indices sorted (we use them in reverse order, below)
				final int[] indices = data.getIndices();
				Arrays.sort(indices);

				// remove old elements from model, store them on stack
				final Stack<Object> elements = new Stack<Object>();
				int shift = 0;
				for (int i = indices.length - 1; i >= 0; i--) {
					final int index = indices[i];
					if (index < insertAt) {
						shift--;
					}
					elements.push(listModel.remove(index));
				}
				insertAt += shift;

				// insert stored elements from stack to model
				final ListSelectionModel sm = list.getSelectionModel();
				try {
					sm.setValueIsAdjusting(true);
					sm.clearSelection();
					final int anchor = insertAt;
					while (!elements.isEmpty()) {
						listModel.insertElementAt(elements.pop(), insertAt);
						sm.addSelectionInterval(insertAt, insertAt++);
					}
					final int lead = insertAt - 1;
					if (!sm.isSelectionEmpty()) {
						sm.setAnchorSelectionIndex(anchor);
						sm.setLeadSelectionIndex(lead);
					}
				} finally {
					sm.setValueIsAdjusting(false);
				}
				return true;
			} catch (UnsupportedFlavorException ex) {
				return false;
			} catch (IOException ex) {
				// FIXME: Logging
				return false;
			}
		} */
	} 

	public static void main(String[] args) {
		final DefaultListModel lm1 = new DefaultListModel();
		for (Object o : new Object[] { "A", "B", "C", "D", "E", "F", "G", "H" }) {
			lm1.addElement(o);
		}
		final JComponent sp1 = createListAndScrollPane(lm1);

		final JPanel indiPanel = new JPanel(new BorderLayout(6, 6));
		indiPanel.add(sp1, BorderLayout.CENTER);

		final JFrame f = new JFrame("Test Frame: List DnD Fun"); //$NON-NLS-1$
		f.setContentPane(indiPanel);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	private static JScrollPane createListAndScrollPane(DefaultListModel model) {
		final JList list = new JList(model);
		list.setDragEnabled(true);
		//TODO Uncomment
		//list.setDropMode(DropMode.INSERT);
		list.setTransferHandler(new ListMoveTransferHandler());
		list.setPrototypeCellValue("WWWWWWWWWWWWWWWWWW");
		final JScrollPane sp = new JScrollPane(list);
		return sp;
	}
}